package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.port.IdentityCredentialValidator;
import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.AuthSessaoEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.AuthenticatedUserResponse;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.ColaboradorOrganizationalLinksResponse;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityProviderClient;
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
    private final IdentityCredentialValidator identityCredentialValidator;
    private final ColaboradorService colaboradorService;
    private final SessionService sessionService;
    private final JwtTokenService jwtTokenService;
    private final AuthCookieService authCookieService;
    private final AuthAuditService authAuditService;
    private final AuthProperties authProperties;

    public AuthenticationService(
            OAuthStateService oAuthStateService,
            IdentityProviderClient identityProviderClient,
            IdentityCredentialValidator identityCredentialValidator,
            ColaboradorService colaboradorService,
            SessionService sessionService,
            JwtTokenService jwtTokenService,
            AuthCookieService authCookieService,
            AuthAuditService authAuditService,
            AuthProperties authProperties) {
        this.oAuthStateService = oAuthStateService;
        this.identityProviderClient = identityProviderClient;
        this.identityCredentialValidator = identityCredentialValidator;
        this.colaboradorService = colaboradorService;
        this.sessionService = sessionService;
        this.jwtTokenService = jwtTokenService;
        this.authCookieService = authCookieService;
        this.authAuditService = authAuditService;
        this.authProperties = authProperties;
    }

    /**
     * Inicia fluxo de login redirecionando à página de credenciais do Portal (state anti-CSRF).
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
     * Valida credenciais no Zimbra e conclui login (emissão de cookies e redirect).
     */
    @Transactional
    public URI authenticateWithCredentials(
            String email,
            String password,
            boolean rememberMe,
            String state,
            HttpServletRequest request,
            HttpServletResponse response) {
        boolean resolvedRememberMe = rememberMe;
        if (state != null && !state.isBlank()) {
            try {
                resolvedRememberMe = oAuthStateService.consumeState(state);
            } catch (ValidationException ex) {
                authAuditService.logLoginFailure("invalid_state");
                throw ex;
            }
        }

        IdentityValidationResult identity;
        try {
            identity = identityCredentialValidator.validateCredentials(email, password);
        } catch (IntegrationUnavailableException ex) {
            authAuditService.logIdentityProviderUnavailable();
            authAuditService.logLoginFailure("zimbra_unavailable");
            throw ex;
        } catch (RuntimeException ex) {
            authAuditService.logLoginFailure("invalid_identity");
            throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
        }

        return finalizeLogin(identity, resolvedRememberMe, request, response);
    }

    /**
     * Processa callback (token opaco), emite tokens e redireciona ao frontend.
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
            identity = identityCredentialValidator.validateOpaqueToken(token);
        } catch (IntegrationUnavailableException ex) {
            authAuditService.logIdentityProviderUnavailable();
            authAuditService.logLoginFailure("zimbra_unavailable");
            throw ex;
        } catch (RuntimeException ex) {
            authAuditService.logLoginFailure("invalid_identity");
            throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
        }

        return finalizeLogin(identity, rememberMe, request, response);
    }

    private URI finalizeLogin(
            IdentityValidationResult identity,
            boolean rememberMe,
            HttpServletRequest request,
            HttpServletResponse response) {
        ColaboradorEntity colaborador = colaboradorService.locateOrCreate(identity);
        if (!colaborador.isAtivo()) {
            authAuditService.logLoginFailure("colaborador_inativo");
            throw new ForbiddenException(FORBIDDEN_MESSAGE);
        }

        String dispositivo = request.getHeader("User-Agent");
        SessionService.SessionCreationResult session = sessionService.createSession(colaborador, rememberMe, dispositivo);

        String accessToken = issueAccessToken(colaborador, session.sessionId());

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

        String accessToken = issueAccessToken(colaborador, sessao.getSessionId());

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
                principal.sessionId(),
                organizationalLinksFrom(colaborador));
    }

    private String issueAccessToken(ColaboradorEntity colaborador, String sessionId) {
        return jwtTokenService.issueToken(
                colaborador.getId(),
                sessionId,
                colaborador.getEmail(),
                colaborador.getNome(),
                colaborador.getFederacaoId(),
                colaborador.getSingularId(),
                colaborador.getAreaId(),
                colaborador.getEquipeId());
    }

    static ColaboradorOrganizationalLinksResponse organizationalLinksFrom(ColaboradorEntity colaborador) {
        return new ColaboradorOrganizationalLinksResponse(
                colaborador.getFederacaoId(),
                colaborador.getSingularId(),
                colaborador.getAreaId(),
                colaborador.getEquipeId());
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
