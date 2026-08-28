package br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.AuthenticationService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.OAuthStateService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.RefreshTokenService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.SessionService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.JwtClaims;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.AuthSessaoEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.AuthSessaoRepository;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.SingularTestBuilder;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.configuration.properties.SecurityProperties;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityProviderClient;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationResult;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import br.com.unimedceara.portalcomunicacao.shared.exception.UnauthorizedException;
import br.com.unimedceara.portalcomunicacao.support.data.IntegrationTestUniqueData;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.base.AbstractTransactionalMockMvcIntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.ColaboradorTestBuilder;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Suíte de testes automatizados dos critérios de aceite FT-AUTH.
 * Rastreabilidade: {@code specs/features/authentication/acceptance-tests.md}
 */
@IntegrationTest
@Import(TestIdentityProviderConfiguration.class)
class AuthAcceptanceIntegrationTest extends AbstractTransactionalMockMvcIntegrationTest {

    @Autowired
    private OAuthStateService oAuthStateService;

    @Autowired
    private IdentityProviderClient identityProviderClient;

    private TestIdentityProviderClient testIdentityProviderClient() {
        return (TestIdentityProviderClient) identityProviderClient;
    }

    @Autowired
    private AuthSessaoRepository authSessaoRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private SingularRepository singularRepository;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthProperties authProperties;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JsonMapper jsonMapper;

    /**
     * E-mail do usuário válido para cada método de teste. Único por execução
     * ({@link IntegrationTestUniqueData}) — não depende de nenhuma identidade pré-existente
     * no schema compartilhado (Oracle TST, sem limpeza automática — DEC-DB-023).
     */
    private String validUserEmail;

    /**
     * Registra o e-mail do usuário válido (único por execução) como administrador de sessão,
     * substituindo a lista fixa de {@code application-test.yaml} — necessário para
     * {@code acAuth010} ({@code DELETE /api/v1/admin/sessions/**}).
     */
    @DynamicPropertySource
    static void sessionAdministratorEmails(DynamicPropertyRegistry registry) {
        registry.add("application.auth.session-administrator-emails",
                () -> TestIdentityProviderClient.DEFAULT_VALID_TOKEN_EMAIL);
    }

    @BeforeEach
    void resetIdentityProvider() {
        testIdentityProviderClient().reset();
        validUserEmail = testIdentityProviderClient().validTokenEmail();
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-001", type = AcceptanceCriterion.TestType.API)
    void acAuth001_shouldAuthenticateSuccessfullyWithHttpOnlyCookies() throws Exception {
        int validationsBefore = testIdentityProviderClient().getValidationCallCount();

        MvcResult callbackResult = performSuccessfulCallback(false);
        Cookie accessCookie = findCookie(callbackResult, SecurityConstants.ACCESS_TOKEN_COOKIE);
        Cookie refreshCookie = findCookie(callbackResult, SecurityConstants.REFRESH_TOKEN_COOKIE);

        assertThat(accessCookie).isNotNull();
        assertThat(refreshCookie).isNotNull();
        assertThat(testIdentityProviderClient().getValidationCallCount()).isEqualTo(validationsBefore + 1);

        mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value(validUserEmail))
                .andExpect(jsonPath("$.data.name").value("Colaborador Teste"))
                .andExpect(jsonPath("$.data.permissions").isArray())
                .andExpect(jsonPath("$.data.sessionId").isNotEmpty());

        int validationsAfterMe = testIdentityProviderClient().getValidationCallCount();
        mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie)).andExpect(status().isOk());
        assertThat(testIdentityProviderClient().getValidationCallCount())
                .as("Zimbra não deve ser consultado após o callback")
                .isEqualTo(validationsAfterMe);
    }

    @Test
    void shouldAuthenticateWithCredentialsViaPostLogin() throws Exception {
        ensureColaboradorExists();
        MvcResult result = mockMvc.perform(
                        post("/api/v1/auth/login")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("email", validUserEmail)
                                .param("password", "secret")
                                .param("remember_me", "false"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("Location"))
                .andReturn();

        Cookie accessCookie = findCookie(result, SecurityConstants.ACCESS_TOKEN_COOKIE);
        assertThat(accessCookie).isNotNull();

        mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(validUserEmail));
    }

    @Test
    void shouldAuthenticatePrimeiroAcessoViaPostLogin() throws Exception {
        long sessionsBefore = authSessaoRepository.count();

        MvcResult result = mockMvc.perform(
                        post("/api/v1/auth/login")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("email", "novo@sem-mapeamento.test")
                                .param("password", "secret")
                                .param("remember_me", "false"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("Location"))
                .andReturn();

        Cookie accessCookie = findCookie(result, SecurityConstants.ACCESS_TOKEN_COOKIE);
        Cookie refreshCookie = findCookie(result, SecurityConstants.REFRESH_TOKEN_COOKIE);
        assertThat(accessCookie).isNotNull();
        assertThat(accessCookie.getValue()).isNotBlank();
        assertThat(refreshCookie == null || refreshCookie.getValue() == null || refreshCookie.getValue().isBlank())
                .as("Primeiro Acesso não emite refresh operacional")
                .isTrue();
        assertThat(authSessaoRepository.count()).isEqualTo(sessionsBefore);
        assertThat(colaboradorRepository.findByEmailIgnoreCase("novo@sem-mapeamento.test")).isEmpty();

        Optional<JwtClaims> claims = jwtTokenService.validateAndParse(accessCookie.getValue());
        assertThat(claims).isPresent();
        assertThat(claims.get().primeiroAcesso()).isTrue();

        mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("novo@sem-mapeamento.test"))
                .andExpect(jsonPath("$.data.primeiroAcesso").value(true))
                .andExpect(jsonPath("$.data.primeiroAcessoBlockCode")
                        .value(SecurityConstants.PA_DOMAIN_NO_SINGULAR));
    }

    @Test
    void shouldRejectInvalidCredentialsOnPostLogin() throws Exception {
        long sessionsBefore = authSessaoRepository.count();

        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "invalid@unimedceara.com.br")
                        .param("password", "wrong")
                        .param("remember_me", "false"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"));

        assertThat(authSessaoRepository.count()).isEqualTo(sessionsBefore);
    }

    @Test
    void shouldRejectInactiveColaboradorOnPostLogin() throws Exception {
        seedInactiveColaborador();
        long sessionsBefore = authSessaoRepository.count();

        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "inactive@unimedceara.com.br")
                        .param("password", "secret")
                        .param("remember_me", "false"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("FORBIDDEN"));

        assertThat(authSessaoRepository.count()).isEqualTo(sessionsBefore);
    }

    @Test
    void shouldReturnServiceUnavailableWhenZimbraUnavailableOnPostLogin() throws Exception {
        long sessionsBefore = authSessaoRepository.count();

        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "unavailable@unimedceara.com.br")
                        .param("password", "secret")
                        .param("remember_me", "false"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("INTEGRATION_UNAVAILABLE"));

        assertThat(authSessaoRepository.count()).isEqualTo(sessionsBefore);
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-002", type = AcceptanceCriterion.TestType.API)
    void acAuth002_shouldRejectInvalidCredentialsWithoutSession() throws Exception {
        long sessionsBefore = authSessaoRepository.count();
        String state = oAuthStateService.createState(false);

        mockMvc.perform(get("/api/v1/auth/callback")
                        .param("token", TestIdentityProviderClient.INVALID_CREDENTIALS_TOKEN)
                        .param("state", state))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"));

        assertThat(authSessaoRepository.count()).isEqualTo(sessionsBefore);
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-003", type = AcceptanceCriterion.TestType.API)
    void acAuth003_shouldRejectInactiveColaboradorWithForbidden() throws Exception {
        seedInactiveColaborador();
        long sessionsBefore = authSessaoRepository.count();
        String state = oAuthStateService.createState(false);

        mockMvc.perform(get("/api/v1/auth/callback")
                        .param("token", TestIdentityProviderClient.INACTIVE_COLABORADOR_TOKEN)
                        .param("state", state))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("FORBIDDEN"));

        assertThat(authSessaoRepository.count()).isEqualTo(sessionsBefore);
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-004", type = AcceptanceCriterion.TestType.API)
    void acAuth004_shouldLogoutRevokeRefreshTokenAndClearCookies() throws Exception {
        MvcResult callbackResult = performSuccessfulCallback(false);
        Cookie accessCookie = findCookie(callbackResult, SecurityConstants.ACCESS_TOKEN_COOKIE);
        Cookie refreshCookie = findCookie(callbackResult, SecurityConstants.REFRESH_TOKEN_COOKIE);
        mockMvc.perform(post("/api/v1/auth/logout")
                        .with(csrf())
                        .cookie(accessCookie, refreshCookie))
                .andExpect(status().isNoContent());

        String refreshHash = hashRefreshToken(refreshCookie.getValue());
        AuthSessaoEntity sessao = authSessaoRepository.findByRefreshTokenHash(refreshHash).orElseThrow();
        assertThat(sessao.isRevogada()).isTrue();

        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-005", type = AcceptanceCriterion.TestType.API)
    void acAuth005_shouldRenewAccessTokenWhenExpired() throws Exception {
        MvcResult callbackResult = performSuccessfulCallback(false);
        Cookie refreshCookie = findCookie(callbackResult, SecurityConstants.REFRESH_TOKEN_COOKIE);

        MvcResult meResult = mockMvc.perform(get("/api/v1/auth/me")
                        .cookie(findCookie(callbackResult, SecurityConstants.ACCESS_TOKEN_COOKIE)))
                .andExpect(status().isOk())
                .andReturn();

        String sessionId = readJsonPath(meResult, "/data/sessionId");
        long colaboradorId = Long.parseLong(readJsonPath(meResult, "/data/id"));
        Cookie expiredAccessCookie = new Cookie(
                SecurityConstants.ACCESS_TOKEN_COOKIE,
                AuthTestTokens.expiredAccessToken(
                        securityProperties,
                        jsonMapper,
                        colaboradorId,
                        sessionId,
                        validUserEmail,
                        "Colaborador Teste"));

        mockMvc.perform(get("/api/v1/auth/me").cookie(expiredAccessCookie))
                .andExpect(status().isUnauthorized());

        MvcResult refreshResult = mockMvc.perform(post("/api/v1/auth/refresh")
                        .with(csrf())
                        .cookie(refreshCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Access token renovado"))
                .andReturn();

        Cookie newAccessCookie = findCookie(refreshResult, SecurityConstants.ACCESS_TOKEN_COOKIE);
        assertThat(newAccessCookie).isNotNull();

        mockMvc.perform(get("/api/v1/auth/me").cookie(newAccessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.sessionId").value(sessionId));
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-006", type = AcceptanceCriterion.TestType.API)
    void acAuth006_shouldReturnServiceUnavailableWhenZimbraUnavailableOnLogin() throws Exception {
        testIdentityProviderClient().setAuthorizationUnavailable(true);

        mockMvc.perform(get("/api/v1/auth/login"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("INTEGRATION_UNAVAILABLE"));
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-007", type = AcceptanceCriterion.TestType.API)
    void acAuth007_shouldReturnAuthenticatedUserViaApiResponse() throws Exception {
        MvcResult callbackResult = performSuccessfulCallback(false);
        Cookie accessCookie = findCookie(callbackResult, SecurityConstants.ACCESS_TOKEN_COOKIE);

        mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.email").value(validUserEmail))
                .andExpect(jsonPath("$.data.name").value("Colaborador Teste"))
                .andExpect(jsonPath("$.data.permissions").isArray())
                .andExpect(jsonPath("$.data.sessionId").isNotEmpty())
                .andExpect(jsonPath("$.data.primeiroAcesso").value(false))
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-008", type = AcceptanceCriterion.TestType.API)
    void acAuth008_shouldRefreshAccessTokenWithValidRefreshToken() throws Exception {
        MvcResult callbackResult = performSuccessfulCallback(false);
        Cookie refreshCookie = findCookie(callbackResult, SecurityConstants.REFRESH_TOKEN_COOKIE);

        String originalRefreshValue = refreshCookie.getValue();

        MvcResult refreshResult = mockMvc.perform(post("/api/v1/auth/refresh")
                        .with(csrf())
                        .cookie(refreshCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();

        Cookie newAccessCookie = findCookie(refreshResult, SecurityConstants.ACCESS_TOKEN_COOKIE);
        assertThat(newAccessCookie).isNotNull();

        mockMvc.perform(get("/api/v1/auth/me").cookie(newAccessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(validUserEmail));

        AuthSessaoEntity sessao = authSessaoRepository
                .findByRefreshTokenHash(hashRefreshToken(originalRefreshValue))
                .orElseThrow();
        assertThat(sessao.isActive()).isTrue();
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-009", type = AcceptanceCriterion.TestType.API)
    void acAuth009_shouldRejectExpiredRefreshToken() throws Exception {
        MvcResult callbackResult = performSuccessfulCallback(false);
        Cookie refreshCookie = findCookie(callbackResult, SecurityConstants.REFRESH_TOKEN_COOKIE);

        AuthSessaoEntity sessao = authSessaoRepository
                .findByRefreshTokenHash(hashRefreshToken(refreshCookie.getValue()))
                .orElseThrow();
        sessao.setDataExpiracao(Instant.now().minus(1, ChronoUnit.HOURS));
        authSessaoRepository.save(sessao);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .with(csrf())
                        .cookie(refreshCookie))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"));
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-010", type = AcceptanceCriterion.TestType.API)
    void acAuth010_shouldRejectRefreshAfterAdministrativeRevocation() throws Exception {
        MvcResult callbackResult = performSuccessfulCallback(false);
        Cookie accessCookie = findCookie(callbackResult, SecurityConstants.ACCESS_TOKEN_COOKIE);
        Cookie refreshCookie = findCookie(callbackResult, SecurityConstants.REFRESH_TOKEN_COOKIE);

        MvcResult meResult = mockMvc.perform(get("/api/v1/auth/me")
                        .cookie(accessCookie))
                .andExpect(status().isOk())
                .andReturn();

        String sessionId = readJsonPath(meResult, "/data/sessionId");

        mockMvc.perform(delete("/api/v1/admin/sessions/{sessionId}", sessionId)
                        .with(csrf())
                        .cookie(accessCookie))
                .andExpect(status().isNoContent());

        AuthSessaoEntity sessao = authSessaoRepository.findBySessionId(sessionId).orElseThrow();
        assertThat(sessao.isRevogada()).isTrue();

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .with(csrf())
                        .cookie(refreshCookie))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-011", type = AcceptanceCriterion.TestType.INTEGRATION)
    void acAuth011_shouldRevokeOldestSessionWhenFourthDeviceLogsIn() throws Exception {
        ColaboradorEntity colaborador = ensureColaboradorExists();

        for (int device = 1; device <= 3; device++) {
            createSessionForColaborador(colaborador, "device-" + device, false);
        }

        List<AuthSessaoEntity> beforeFourth = activeSessions(colaborador.getId());
        assertThat(beforeFourth).hasSize(3);
        AuthSessaoEntity oldest = beforeFourth.getFirst();

        performSuccessfulCallback(false);

        List<AuthSessaoEntity> afterFourth = activeSessions(colaborador.getId());
        assertThat(afterFourth).hasSize(3);

        AuthSessaoEntity revokedOldest = authSessaoRepository.findById(oldest.getId()).orElseThrow();
        assertThat(revokedOldest.isRevogada()).isTrue();
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-012", type = AcceptanceCriterion.TestType.API)
    void acAuth012_shouldSetHttpOnlyAndSecureFlagsOnTokenCookies() throws Exception {
        MvcResult callbackResult = performSuccessfulCallback(false);

        String accessSetCookie = findSetCookieHeader(callbackResult, SecurityConstants.ACCESS_TOKEN_COOKIE);
        String refreshSetCookie = findSetCookieHeader(callbackResult, SecurityConstants.REFRESH_TOKEN_COOKIE);

        assertThat(accessSetCookie).containsIgnoringCase("HttpOnly");
        assertThat(accessSetCookie).containsIgnoringCase("Secure");
        assertThat(refreshSetCookie).containsIgnoringCase("HttpOnly");
        assertThat(refreshSetCookie).containsIgnoringCase("Secure");
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-013", type = AcceptanceCriterion.TestType.API)
    void acAuth013_shouldExtendRefreshTokenTtlWithRememberMe() throws Exception {
        ensureColaboradorExists();
        String state = oAuthStateService.createState(true);

        MvcResult callbackResult = mockMvc.perform(get("/api/v1/auth/callback")
                        .param("token", TestIdentityProviderClient.VALID_TOKEN)
                        .param("state", state))
                .andExpect(status().isFound())
                .andReturn();

        Cookie refreshCookie = findCookie(callbackResult, SecurityConstants.REFRESH_TOKEN_COOKIE);
        AuthSessaoEntity sessao = authSessaoRepository
                .findByRefreshTokenHash(hashRefreshToken(refreshCookie.getValue()))
                .orElseThrow();

        assertThat(sessao.isRememberMe()).isTrue();
        assertThat(sessao.getDataExpiracao())
                .isAfter(Instant.now().plus(securityProperties.refreshTokenRememberMeDays() - 1, ChronoUnit.DAYS));

        String refreshSetCookie = findSetCookieHeader(callbackResult, SecurityConstants.REFRESH_TOKEN_COOKIE);
        long expectedMaxAgeSeconds = securityProperties.refreshTokenRememberMeDays() * 24L * 60L * 60L;
        assertThat(refreshSetCookie).contains("Max-Age=" + expectedMaxAgeSeconds);
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-014", type = AcceptanceCriterion.TestType.API)
    void acAuth014_shouldHandleZimbraFailureOnCallback() throws Exception {
        long sessionsBefore = authSessaoRepository.count();
        String state = oAuthStateService.createState(false);

        mockMvc.perform(get("/api/v1/auth/callback")
                        .param("token", TestIdentityProviderClient.ZIMBRA_UNAVAILABLE_TOKEN)
                        .param("state", state))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("INTEGRATION_UNAVAILABLE"));

        assertThat(authSessaoRepository.count()).isEqualTo(sessionsBefore);
    }

    @Test
    void shouldIssuePrimeiroAcessoCredentialWhenColaboradorIsMissing() throws Exception {
        testIdentityProviderClient().setValidationBehavior(token -> {
            if (TestIdentityProviderClient.VALID_TOKEN.equals(token)) {
                return new IdentityValidationResult(
                        "novo@sem-mapeamento.test", "Novo Usuario", "zimbra-pa-unknown");
            }
            throw new UnauthorizedException("Autenticação não realizada");
        });

        long sessionsBefore = authSessaoRepository.count();
        String state = oAuthStateService.createState(false);

        MvcResult callbackResult = mockMvc.perform(get("/api/v1/auth/callback")
                        .param("token", TestIdentityProviderClient.VALID_TOKEN)
                        .param("state", state))
                .andExpect(status().isFound())
                .andReturn();

        Cookie accessCookie = findCookie(callbackResult, SecurityConstants.ACCESS_TOKEN_COOKIE);
        Cookie refreshCookie = findCookie(callbackResult, SecurityConstants.REFRESH_TOKEN_COOKIE);
        assertThat(accessCookie).isNotNull();
        assertThat(accessCookie.getValue()).isNotBlank();
        assertThat(refreshCookie == null || refreshCookie.getValue() == null || refreshCookie.getValue().isBlank())
                .as("Primeiro Acesso não emite refresh operacional")
                .isTrue();
        assertThat(authSessaoRepository.count()).isEqualTo(sessionsBefore);
        assertThat(colaboradorRepository.findByEmailIgnoreCase("novo@sem-mapeamento.test")).isEmpty();

        mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("novo@sem-mapeamento.test"))
                .andExpect(jsonPath("$.data.id").doesNotExist())
                .andExpect(jsonPath("$.data.sessionId").doesNotExist())
                .andExpect(jsonPath("$.data.organizationalLinks").doesNotExist())
                .andExpect(jsonPath("$.data.primeiroAcesso").value(true))
                .andExpect(jsonPath("$.data.resolvedOrganization").doesNotExist())
                .andExpect(jsonPath("$.data.primeiroAcessoBlockCode")
                        .value(SecurityConstants.PA_DOMAIN_NO_SINGULAR));

        mockMvc.perform(get("/api/v1/federacoes").cookie(accessCookie))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("FORBIDDEN"));
    }

    @Test
    void shouldPromotePrimeiroAcessoCredentialToOperationalSession() throws Exception {
        long sessionsBefore = authSessaoRepository.count();
        String state = oAuthStateService.createState(false);
        MvcResult paResult = mockMvc.perform(get("/api/v1/auth/callback")
                        .param("token", TestIdentityProviderClient.VALID_TOKEN)
                        .param("state", state)
                        .header("User-Agent", "pa-agent"))
                .andExpect(status().isFound())
                .andReturn();

        Cookie paAccess = findCookie(paResult, SecurityConstants.ACCESS_TOKEN_COOKIE);
        assertThat(paAccess).isNotNull();

        ColaboradorEntity colaborador = ensureColaboradorExists();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("User-Agent", "pa-agent");
        MockHttpServletResponse response = new MockHttpServletResponse();

        authenticationService.promoteToOperationalSession(colaborador, false, request, response);

        Cookie operationalAccess = cookieFromSetCookieHeaders(response, SecurityConstants.ACCESS_TOKEN_COOKIE);
        Cookie operationalRefresh = cookieFromSetCookieHeaders(response, SecurityConstants.REFRESH_TOKEN_COOKIE);
        assertThat(operationalAccess).isNotNull();
        assertThat(operationalRefresh).isNotNull();
        assertThat(authSessaoRepository.count()).isEqualTo(sessionsBefore + 1);

        mockMvc.perform(get("/api/v1/auth/me").cookie(operationalAccess))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(colaborador.getId().intValue()))
                .andExpect(jsonPath("$.data.primeiroAcesso").value(false))
                .andExpect(jsonPath("$.data.sessionId").isNotEmpty());
    }

    @Test
    void shouldResolveSingularFromAuthenticatedEmailDomain() throws Exception {
        String domain = "pa-known-" + IntegrationTestUniqueData.singularSigla("dm").toLowerCase() + ".test";
        SingularEntity singular = SingularTestBuilder.forFederation(authProperties.defaultFederationId())
                .dominioEmail(domain)
                .persist(singularRepository);
        String email = "User@" + domain;
        Cookie accessCookie = primeiroAcessoCookieForEmail(email);

        mockMvc.perform(get("/api/v1/auth/me")
                        .param("domain", "outro.dominio.test")
                        .param("singularId", "999999")
                        .cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.primeiroAcesso").value(true))
                .andExpect(jsonPath("$.data.primeiroAcessoBlockCode").doesNotExist())
                .andExpect(jsonPath("$.data.resolvedOrganization.singularId").value(singular.getId().intValue()))
                .andExpect(jsonPath("$.data.resolvedOrganization.federationId")
                        .value(singular.getFederacaoId().intValue()))
                .andExpect(jsonPath("$.data.organizationalLinks").doesNotExist());
    }

    @Test
    void shouldBlockPrimeiroAcessoWhenEmailDomainHasNoSingular() throws Exception {
        Cookie accessCookie = primeiroAcessoCookieForEmail("user@dominio-inexistente.test");

        mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.primeiroAcesso").value(true))
                .andExpect(jsonPath("$.data.resolvedOrganization").doesNotExist())
                .andExpect(jsonPath("$.data.primeiroAcessoBlockCode")
                        .value(SecurityConstants.PA_DOMAIN_NO_SINGULAR));
    }

    @Test
    void shouldBlockPrimeiroAcessoWhenSingularForDomainIsInactive() throws Exception {
        String domain = "pa-inactive-" + IntegrationTestUniqueData.singularSigla("dm").toLowerCase() + ".test";
        SingularEntity singular = SingularTestBuilder.forFederation(authProperties.defaultFederationId())
                .dominioEmail(domain)
                .persist(singularRepository);
        singular.setAtivo(SingularStatus.INACTIVE.toFlag());
        singularRepository.save(singular);

        Cookie accessCookie = primeiroAcessoCookieForEmail("user@" + domain);

        mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.primeiroAcesso").value(true))
                .andExpect(jsonPath("$.data.resolvedOrganization").doesNotExist())
                .andExpect(jsonPath("$.data.primeiroAcessoBlockCode")
                        .value(SecurityConstants.PA_DOMAIN_NO_SINGULAR));
    }

    @Test
    void shouldRejectDuplicateSingularEmailDomain() {
        String domain = "pa-dup-" + IntegrationTestUniqueData.singularSigla("dm").toLowerCase() + ".test";
        SingularTestBuilder.forFederation(authProperties.defaultFederationId())
                .dominioEmail(domain)
                .persist(singularRepository);
        singularRepository.flush();

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
                    SingularTestBuilder.forFederation(authProperties.defaultFederationId())
                            .dominioEmail(domain)
                            .persist(singularRepository);
                    // flush explícito: o INSERT é diferido (id por SEQUENCE), então a violação de
                    // UK_SINGULAR_DOMINIO_EMAIL só se materializa aqui, dentro do assertThatThrownBy.
                    singularRepository.flush();
                })
                .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
    }

    private Cookie primeiroAcessoCookieForEmail(String email) throws Exception {
        testIdentityProviderClient().setValidationBehavior(token -> {
            if (TestIdentityProviderClient.VALID_TOKEN.equals(token)) {
                return new IdentityValidationResult(email, "Usuario PA", "zimbra-pa-" + email);
            }
            throw new UnauthorizedException("Autenticação não realizada");
        });
        String state = oAuthStateService.createState(false);
        MvcResult callbackResult = mockMvc.perform(get("/api/v1/auth/callback")
                        .param("token", TestIdentityProviderClient.VALID_TOKEN)
                        .param("state", state))
                .andExpect(status().isFound())
                .andReturn();
        Cookie accessCookie = findCookie(callbackResult, SecurityConstants.ACCESS_TOKEN_COOKIE);
        assertThat(accessCookie).isNotNull();
        return accessCookie;
    }

    private MvcResult performSuccessfulCallback(boolean rememberMe) throws Exception {
        ensureColaboradorExists();
        String state = oAuthStateService.createState(rememberMe);
        return mockMvc.perform(get("/api/v1/auth/callback")
                        .param("token", TestIdentityProviderClient.VALID_TOKEN)
                        .param("state", state)
                        .header("User-Agent", "acceptance-test-agent"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", authProperties.frontendRedirectUrl()))
                .andReturn();
    }

    private void seedInactiveColaborador() {
        ColaboradorTestBuilder.forFederation(authProperties.defaultFederationId())
                .email("inactive@unimedceara.com.br")
                .nome("Colaborador Inativo")
                .zimbraId("zimbra-id-inactive")
                .ativo("N")
                .persist(colaboradorRepository);
    }

    private ColaboradorEntity ensureColaboradorExists() {
        return colaboradorRepository.findByEmailIgnoreCase(validUserEmail)
                .orElseGet(() -> ColaboradorTestBuilder.forFederation(authProperties.defaultFederationId())
                        .email(validUserEmail)
                        .nome("Colaborador Teste")
                        .zimbraId("zimbra-" + validUserEmail)
                        .persist(colaboradorRepository));
    }

    private void createSessionForColaborador(ColaboradorEntity colaborador, String dispositivo, boolean rememberMe) {
        sessionService.createSession(colaborador, rememberMe, dispositivo);
    }

    private List<AuthSessaoEntity> activeSessions(long colaboradorId) {
        return authSessaoRepository.findByColaborador_IdAndRevogadaAndDataExpiracaoAfterOrderByDataCriacaoAsc(
                colaboradorId, "N", Instant.now());
    }

    private String hashRefreshToken(String rawToken) {
        return refreshTokenService.hashToken(rawToken);
    }

    private Cookie findCookie(MvcResult result, String name) {
        if (result.getResponse().getCookies() == null) {
            return cookieFromSetCookieHeaders(result.getResponse(), name);
        }
        for (Cookie cookie : result.getResponse().getCookies()) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return cookieFromSetCookieHeaders(result.getResponse(), name);
    }

    private Cookie cookieFromSetCookieHeaders(jakarta.servlet.http.HttpServletResponse response, String cookieName) {
        Collection<String> setCookies = response.getHeaders("Set-Cookie");
        return setCookies.stream()
                .filter(header -> header.startsWith(cookieName + "="))
                .map(header -> {
                    String value = header.substring(cookieName.length() + 1).split(";", 2)[0];
                    return new Cookie(cookieName, value);
                })
                .findFirst()
                .orElse(null);
    }

    private String findSetCookieHeader(MvcResult result, String cookieName) {
        List<String> setCookies = result.getResponse().getHeaders("Set-Cookie");
        return setCookies.stream()
                .filter(header -> header.startsWith(cookieName + "="))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Set-Cookie não encontrado para " + cookieName));
    }

    private String readJsonPath(MvcResult result, String jsonPointer) throws Exception {
        JsonNode root = jsonMapper.readTree(result.getResponse().getContentAsString());
        JsonNode node = root.at(jsonPointer.replace("$", ""));
        if (node.isMissingNode() || node.isNull()) {
            throw new AssertionError("JSON path not found: " + jsonPointer);
        }
        return node.isNumber() ? node.numberValue().toString() : node.asText();
    }
}
