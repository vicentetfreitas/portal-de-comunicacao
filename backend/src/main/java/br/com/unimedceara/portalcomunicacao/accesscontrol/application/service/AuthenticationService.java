package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.port.IdentityCredentialValidator;
import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtAuthenticatedPrincipal;
import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtClaims;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.AuthSessaoEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.PapelAtribuicaoEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.AuthenticatedUserResponse;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.ColaboradorOrganizationalLinksResponse;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.PapelAtribuicaoResponse;
import br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest.dto.ResolvedPrimeiroAcessoOrganization;
import br.com.unimedceara.portalcomunicacao.organization.application.service.SingularDomainService;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
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
import java.util.Optional;

/**
 * Orquestração dos fluxos de autenticação FT-AUTH.
 */
@Service
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AuthenticationService {

    private static final String UNAUTHORIZED_MESSAGE = "Autenticação não realizada";
    private static final String FORBIDDEN_MESSAGE = "Colaborador sem autorização para acessar o Portal";
    private static final String REFRESH_EXPIRED_MESSAGE = "Refresh token expirado";
    private static final String INVALID_ASSIGNMENT_MESSAGE = "Atribuição inválida para o colaborador";

    private final OAuthStateService oAuthStateService;
    private final IdentityProviderClient identityProviderClient;
    private final IdentityCredentialValidator identityCredentialValidator;
    private final ColaboradorService colaboradorService;
    private final SingularDomainService singularDomainService;
    private final SessionService sessionService;
    private final PapelAtribuicaoService papelAtribuicaoService;
    private final JwtTokenService jwtTokenService;
    private final AuthCookieService authCookieService;
    private final AuthAuditService authAuditService;
    private final AuthProperties authProperties;

    public AuthenticationService(
            OAuthStateService oAuthStateService,
            IdentityProviderClient identityProviderClient,
            IdentityCredentialValidator identityCredentialValidator,
            ColaboradorService colaboradorService,
            SingularDomainService singularDomainService,
            SessionService sessionService,
            PapelAtribuicaoService papelAtribuicaoService,
            JwtTokenService jwtTokenService,
            AuthCookieService authCookieService,
            AuthAuditService authAuditService,
            AuthProperties authProperties) {
        this.oAuthStateService = oAuthStateService;
        this.identityProviderClient = identityProviderClient;
        this.identityCredentialValidator = identityCredentialValidator;
        this.colaboradorService = colaboradorService;
        this.singularDomainService = singularDomainService;
        this.sessionService = sessionService;
        this.papelAtribuicaoService = papelAtribuicaoService;
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
        Optional<ColaboradorEntity> existing = colaboradorService.findByIdentity(identity);
        if (existing.isEmpty()) {
            return finalizePrimeiroAcesso(identity, response);
        }

        ColaboradorEntity colaborador = existing.get();
        if (!colaborador.isAtivo()) {
            authAuditService.logLoginFailure("colaborador_inativo");
            throw new ForbiddenException(FORBIDDEN_MESSAGE);
        }

        return promoteToOperationalSession(colaborador, rememberMe, request, response);
    }

    private URI finalizePrimeiroAcesso(IdentityValidationResult identity, HttpServletResponse response) {
        String accessToken = jwtTokenService.issuePrimeiroAcessoToken(
                identity.email(),
                identity.displayName(),
                identity.zimbraId());
        authCookieService.setAccessTokenCookie(response, accessToken);
        authCookieService.clearRefreshTokenCookie(response);
        authAuditService.logPrimeiroAcessoLogin(identity.email());
        return URI.create(authProperties.frontendRedirectUrl());
    }

    /**
     * Promove identidade de Primeiro Acesso a sessão operacional após criação do COLABORADOR.
     */
    @Transactional
    public URI promoteToOperationalSession(
            ColaboradorEntity colaborador,
            boolean rememberMe,
            HttpServletRequest request,
            HttpServletResponse response) {
        promoteToOperationalUser(colaborador, rememberMe, request, response);
        return URI.create(authProperties.frontendRedirectUrl());
    }

    /**
     * Promove a sessão operacional e devolve a identidade equivalente a {@code GET /auth/me}.
     */
    @Transactional
    public AuthenticatedUserResponse promoteToOperationalUser(
            ColaboradorEntity colaborador,
            boolean rememberMe,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (!colaborador.isAtivo()) {
            throw new ForbiddenException(FORBIDDEN_MESSAGE);
        }
        Optional<PapelAtribuicaoEntity> activeAssignment = papelAtribuicaoService.resolveAutomatica(colaborador.getId());
        SessionService.SessionCreationResult session = issueOperationalSession(
                colaborador, rememberMe, request, response, activeAssignment.map(PapelAtribuicaoEntity::getId).orElse(null));
        return new AuthenticatedUserResponse(
                colaborador.getId(),
                colaborador.getEmail(),
                colaborador.getNome(),
                loadPermissions(colaborador.getId()),
                session.sessionId(),
                organizationalLinksFrom(colaborador),
                false,
                null,
                null,
                eligibleAssignmentsFrom(colaborador.getId()),
                activeAssignment.map(AuthenticationService::assignmentResponseFrom).orElse(null));
    }

    private SessionService.SessionCreationResult issueOperationalSession(
            ColaboradorEntity colaborador,
            boolean rememberMe,
            HttpServletRequest request,
            HttpServletResponse response,
            Long papelAtribuicaoId) {
        String dispositivo = request.getHeader("User-Agent");
        SessionService.SessionCreationResult session = sessionService.createSession(colaborador, rememberMe, dispositivo);

        String accessToken = issueAccessToken(colaborador, session.sessionId(), papelAtribuicaoId);

        authCookieService.setAccessTokenCookie(response, accessToken);
        authCookieService.setRefreshTokenCookie(response, session.rawRefreshToken(), session.rememberMe());
        authAuditService.logLoginSuccess(colaborador.getId(), session.sessionId());

        return session;
    }

    /**
     * Renova Access Token a partir do Refresh Token do cookie, preservando a atribuição de
     * papel (PAPEL_ATRIBUICAO) ativa enquanto ela continuar elegível (pertencente ao
     * colaborador, ativa e vigente). Se a atribuição anterior deixou de ser elegível, a
     * mesma regra de seleção automática do login é reaplicada (1 elegível → seleciona;
     * 0 ou mais de 1 → sem atribuição ativa, exige seleção explícita).
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

        Long previousAssignmentId = extractPreviousAssignmentId(cookies, colaborador.getId());
        Optional<PapelAtribuicaoEntity> activeAssignment = papelAtribuicaoService
                .resolveParaRefresh(colaborador.getId(), previousAssignmentId);

        String accessToken = issueAccessToken(
                colaborador, sessao.getSessionId(), activeAssignment.map(PapelAtribuicaoEntity::getId).orElse(null));

        authCookieService.setAccessTokenCookie(response, accessToken);
        authAuditService.logRefresh(colaborador.getId(), sessao.getSessionId());
    }

    /**
     * Recupera, sem validar autenticação/expiração, a atribuição ativa reivindicada pelo
     * Access Token anterior (se presente) — apenas como candidata; a elegibilidade é
     * sempre revalidada contra o banco em {@link PapelAtribuicaoService}.
     */
    private Long extractPreviousAssignmentId(Cookie[] cookies, Long colaboradorId) {
        String previousAccessToken = authCookieService.extractAccessToken(cookies);
        if (previousAccessToken == null) {
            return null;
        }
        return jwtTokenService.parseIgnoringExpiration(previousAccessToken)
                .filter(claims -> colaboradorId.equals(claims.colaboradorId()))
                .map(JwtClaims::papelAtribuicaoId)
                .orElse(null);
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
     * Retorna identidade do colaborador autenticado com permissões do banco e o contexto
     * operacional atual: atribuições elegíveis e qual delas está ativa. A atribuição ativa
     * reivindicada pelo token é sempre revalidada contra o banco antes de ser exposta.
     */
    @Transactional(readOnly = true)
    public AuthenticatedUserResponse getAuthenticatedUser(JwtAuthenticatedPrincipal principal) {
        if (principal.primeiroAcesso()) {
            return primeiroAcessoUser(principal);
        }

        if (principal.colaboradorId() == null) {
            throw new ForbiddenException(FORBIDDEN_MESSAGE);
        }

        ColaboradorEntity colaborador = colaboradorService.findById(principal.colaboradorId());
        if (!colaborador.isAtivo()) {
            throw new ForbiddenException(FORBIDDEN_MESSAGE);
        }

        List<String> permissions = loadPermissions(colaborador.getId());
        // RN-SESSION-007 é uma regra de estado (não apenas de login/refresh): se o token não
        // carrega atribuição ativa válida, reaplica a seleção automática (1 elegível) aqui,
        // sem reemitir cookie — a próxima renovação alinha o Access Token a este resultado.
        PapelAtribuicaoResponse activeAssignment = papelAtribuicaoService
                .resolveParaRefresh(colaborador.getId(), principal.papelAtribuicaoId())
                .map(AuthenticationService::assignmentResponseFrom)
                .orElse(null);
        return new AuthenticatedUserResponse(
                colaborador.getId(),
                colaborador.getEmail(),
                colaborador.getNome(),
                permissions,
                principal.sessionId(),
                organizationalLinksFrom(colaborador),
                false,
                null,
                null,
                eligibleAssignmentsFrom(colaborador.getId()),
                activeAssignment);
    }

    /**
     * Ativa (seleciona) uma atribuição de papel do colaborador como contexto operacional,
     * emitindo novo Access Token sem afetar sessão (Refresh Token) nem exigir novo login.
     * A atribuição informada é sempre revalidada contra o banco — pertencimento ao
     * colaborador autenticado, status ativo e vigência — nunca aceita apenas pelo
     * identificador recebido.
     */
    @Transactional
    public AuthenticatedUserResponse selectAtribuicao(
            JwtAuthenticatedPrincipal principal, Long papelAtribuicaoId, HttpServletResponse response) {
        if (principal.primeiroAcesso() || principal.colaboradorId() == null) {
            throw new ForbiddenException(FORBIDDEN_MESSAGE);
        }

        ColaboradorEntity colaborador = colaboradorService.findById(principal.colaboradorId());
        if (!colaborador.isAtivo()) {
            throw new ForbiddenException(FORBIDDEN_MESSAGE);
        }

        PapelAtribuicaoEntity atribuicao = papelAtribuicaoService
                .findElegivel(colaborador.getId(), papelAtribuicaoId)
                .orElseThrow(() -> new ForbiddenException(INVALID_ASSIGNMENT_MESSAGE));

        String accessToken = issueAccessToken(colaborador, principal.sessionId(), atribuicao.getId());
        authCookieService.setAccessTokenCookie(response, accessToken);
        authAuditService.logRefresh(colaborador.getId(), principal.sessionId());

        return new AuthenticatedUserResponse(
                colaborador.getId(),
                colaborador.getEmail(),
                colaborador.getNome(),
                loadPermissions(colaborador.getId()),
                principal.sessionId(),
                organizationalLinksFrom(colaborador),
                false,
                null,
                null,
                eligibleAssignmentsFrom(colaborador.getId()),
                assignmentResponseFrom(atribuicao));
    }

    private AuthenticatedUserResponse primeiroAcessoUser(JwtAuthenticatedPrincipal principal) {
        return singularDomainService.findActiveByAuthenticatedEmail(principal.email())
                .map(singular -> new AuthenticatedUserResponse(
                        null,
                        principal.email(),
                        principal.name(),
                        Collections.emptyList(),
                        null,
                        null,
                        true,
                        new ResolvedPrimeiroAcessoOrganization(singular.getId(), singular.getFederacaoId()),
                        null,
                        Collections.emptyList(),
                        null))
                .orElseGet(() -> new AuthenticatedUserResponse(
                        null,
                        principal.email(),
                        principal.name(),
                        Collections.emptyList(),
                        null,
                        null,
                        true,
                        null,
                        SecurityConstants.PA_DOMAIN_NO_SINGULAR,
                        Collections.emptyList(),
                        null));
    }

    private String issueAccessToken(ColaboradorEntity colaborador, String sessionId, Long papelAtribuicaoId) {
        return jwtTokenService.issueToken(
                colaborador.getId(),
                sessionId,
                colaborador.getEmail(),
                colaborador.getNome(),
                colaborador.getFederacaoId(),
                colaborador.getSingularId(),
                colaborador.getAreaId(),
                colaborador.getEquipeId(),
                papelAtribuicaoId);
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

    private List<PapelAtribuicaoResponse> eligibleAssignmentsFrom(Long colaboradorId) {
        return papelAtribuicaoService.listElegiveis(colaboradorId).stream()
                .map(AuthenticationService::assignmentResponseFrom)
                .toList();
    }

    private static PapelAtribuicaoResponse assignmentResponseFrom(PapelAtribuicaoEntity atribuicao) {
        return new PapelAtribuicaoResponse(
                atribuicao.getId(),
                atribuicao.getPapel().getNome(),
                atribuicao.getFederacaoId(),
                atribuicao.getSingularId(),
                atribuicao.getAreaId(),
                atribuicao.getEquipeId());
    }

    private String buildCallbackUrl(HttpServletRequest request) {
        return ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath("/api/v1/auth/callback")
                .replaceQuery(null)
                .build()
                .toUriString();
    }
}
