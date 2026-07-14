package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.AuthSessaoEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.AuthenticatedUserResponse;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityProviderClient;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationRequest;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationResult;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.exception.IntegrationUnavailableException;
import br.com.unimedceara.portalcomunicacao.shared.exception.ForbiddenException;
import br.com.unimedceara.portalcomunicacao.shared.exception.UnauthorizedException;
import br.com.unimedceara.portalcomunicacao.shared.exception.ValidationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 * Orquestração dos fluxos de autenticação FT-AUTH.
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AuthenticationService {

    private static final String UNAUTHORIZED_MESSAGE = "Autenticação não realizada";
    private static final String FORBIDDEN_MESSAGE = "Colaborador sem autorização para acessar o Portal";
    private static final String REFRESH_EXPIRED_MESSAGE = "Refresh token expirado";

    private final OAuthStateService oAuthStateService;
    private final IdentityProviderClient identityProviderClient;
    private final ColaboradorService colaboradorService;
    private final SessionService sessionService;
    private final JwtTokenService jwtTokenService;
    private final AuthCookieService authCookieService;
    private final AuthAuditService authAuditService;
    private final AuthProperties authProperties;

    public AuthenticationService(
            OAuthStateService oAuthStateService,
            IdentityProviderClient identityProviderClient,
            ColaboradorService colaboradorService,
            SessionService sessionService,
            JwtTokenService jwtTokenService,
            AuthCookieService authCookieService,
            AuthAuditService authAuditService,
            AuthProperties authProperties) {
        this.oAuthStateService = oAuthStateService;
        this.identityProviderClient = identityProviderClient;
        this.colaboradorService = colaboradorService;
        this.sessionService = sessionService;
        this.jwtTokenService = jwtTokenService;
        this.authCookieService = authCookieService;
        this.authAuditService = authAuditService;
        this.authProperties = authProperties;
    }

    /**
     * Inicia fluxo de login redirecionando ao Zimbra com state anti-CSRF.
     */
    public URI initiateLogin(boolean rememberMe, HttpServletRequest request) {
        try {
            String state = oAuthStateService.createState(rememberMe);
            String callbackUrl = buildCallbackUrl(request);
            return identityProviderClient.buildAuthorizationUrl(state, callbackUrl);
        } catch (IntegrationUnavailableException ex) {
            authAuditService.logIdentityProviderUnavailable();
            throw ex;
        }
    }

    /**
     * Processa callback do Zimbra, emite tokens e redireciona ao frontend.
     */
    @Transactional
    public URI handleCallback(String token, String state, HttpServletRequest request,
            HttpServletResponse response) {
        if (token == null || token.isBlank()) {
            authAuditService.logLoginFailure("missing_token");
            throw new ValidationException("Resposta Zimbra inválida", Collections.emptyList());
        }

        boolean rememberMe;
        try {
            rememberMe = oAuthStateService.consumeState(state);
        } catch (ValidationException ex) {
            authAuditService.logLoginFailure("invalid_state");
            throw ex;
        }

        IdentityValidationResult identity;
        try {
            identity = identityProviderClient.validateIdentity(new IdentityValidationRequest(token));
        } catch (IntegrationUnavailableException ex) {
            authAuditService.logIdentityProviderUnavailable();
            authAuditService.logLoginFailure("zimbra_unavailable");
            throw ex;
        } catch (RuntimeException ex) {
            authAuditService.logLoginFailure("invalid_identity");
            throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
        }

        ColaboradorEntity colaborador = colaboradorService.locateOrCreate(identity);
        if (!colaborador.isAtivo()) {
            authAuditService.logLoginFailure("colaborador_inativo");
            throw new ForbiddenException(FORBIDDEN_MESSAGE);
        }

        String dispositivo = request.getHeader("User-Agent");
        SessionService.SessionCreationResult session = sessionService.createSession(colaborador, rememberMe, dispositivo);

        String accessToken = jwtTokenService.issueToken(
                colaborador.getId(),
                session.sessionId(),
                colaborador.getEmail(),
                colaborador.getNome());

        authCookieService.setAccessTokenCookie(response, accessToken);
        authCookieService.setRefreshTokenCookie(response, session.rawRefreshToken(), session.rememberMe());
        authAuditService.logLoginSuccess(colaborador.getId(), session.sessionId());

        return URI.create(authProperties.frontendRedirectUrl());
    }

    /**
     * Renova Access Token a partir do Refresh Token do cookie.
     */
    @Transactional
    public void refreshAccessToken(Cookie[] cookies, HttpServletResponse response) {
        String rawRefreshToken = authCookieService.extractRefreshToken(cookies);
        if (rawRefreshToken == null) {
            authCookieService.clearAuthCookies(response);
            throw new UnauthorizedException(REFRESH_EXPIRED_MESSAGE);
        }

        AuthSessaoEntity sessao = sessionService.findActiveByRefreshToken(rawRefreshToken)
                .orElseThrow(() -> {
                    authCookieService.clearAuthCookies(response);
                    return new UnauthorizedException(REFRESH_EXPIRED_MESSAGE);
                });

        ColaboradorEntity colaborador = sessao.getColaborador();
        if (!colaborador.isAtivo()) {
            sessionService.revokeByRefreshToken(rawRefreshToken);
            authCookieService.clearAuthCookies(response);
            throw new ForbiddenException(FORBIDDEN_MESSAGE);
        }

        String accessToken = jwtTokenService.issueToken(
                colaborador.getId(),
                sessao.getSessionId(),
                colaborador.getEmail(),
                colaborador.getNome());

        authCookieService.setAccessTokenCookie(response, accessToken);
        authAuditService.logRefresh(colaborador.getId(), sessao.getSessionId());
    }

    /**
     * Encerra sessão revogando Refresh Token e removendo cookies.
     */
    @Transactional
    public void logout(Cookie[] cookies, HttpServletResponse response) {
        String rawRefreshToken = authCookieService.extractRefreshToken(cookies);
        if (rawRefreshToken != null) {
            sessionService.findActiveByRefreshToken(rawRefreshToken).ifPresent(sessao -> {
                sessionService.revokeByRefreshToken(rawRefreshToken);
                authAuditService.logLogout(sessao.getColaborador().getId(), sessao.getSessionId());
            });
        }
        authCookieService.clearAuthCookies(response);
    }

    /**
     * Retorna identidade do colaborador autenticado com permissões do banco.
     */
    @Transactional(readOnly = true)
    public AuthenticatedUserResponse getAuthenticatedUser(JwtAuthenticatedPrincipal principal) {
        ColaboradorEntity colaborador = colaboradorService.findById(principal.colaboradorId());
        if (!colaborador.isAtivo()) {
            throw new ForbiddenException(FORBIDDEN_MESSAGE);
        }

        List<String> permissions = loadPermissions(colaborador.getId());
        return new AuthenticatedUserResponse(
                colaborador.getId(),
                colaborador.getEmail(),
                colaborador.getNome(),
                permissions,
                principal.sessionId());
    }

    private List<String> loadPermissions(long colaboradorId) {
        // Permissões serão carregadas de tabelas dedicadas em Features futuras.
        return Collections.emptyList();
    }

    private String buildCallbackUrl(HttpServletRequest request) {
        return ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath("/api/v1/auth/callback")
                .replaceQuery(null)
                .build()
                .toUriString();
    }
}
