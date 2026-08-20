package br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.OAuthStateService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.AuthSessaoRepository;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityProviderClient;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationResult;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import br.com.unimedceara.portalcomunicacao.shared.exception.UnauthorizedException;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.base.AbstractTransactionalMockMvcIntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.data.IntegrationTestUniqueData;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.AreaTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.ColaboradorTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.SingularTestBuilder;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Aceite do onboarding de Primeiro Acesso (PA-API-006 / PA-API-007).
 */
@IntegrationTest
@Import(TestIdentityProviderConfiguration.class)
class PrimeiroAcessoAcceptanceIntegrationTest extends AbstractTransactionalMockMvcIntegrationTest {

    @Autowired
    private OAuthStateService oAuthStateService;

    @Autowired
    private IdentityProviderClient identityProviderClient;

    @Autowired
    private AuthProperties authProperties;

    @Autowired
    private SingularRepository singularRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private AuthSessaoRepository authSessaoRepository;

    @BeforeEach
    void resetIdentityProvider() {
        testIdentityProviderClient().reset();
    }

    @Test
    @AcceptanceCriterion(value = "AT-PA-003", type = AcceptanceCriterion.TestType.API)
    void shouldListOnlyActiveAreasOfResolvedSingular() throws Exception {
        PaContext context = seedMappedPrimeiroAcesso();
        AreaEntity ownActive = AreaTestBuilder.forSingular(context.singular().getId())
                .nome("Área Própria")
                .persist(areaRepository);
        AreaTestBuilder.forSingular(context.singular().getId())
                .nome("Área Inativa")
                .inativa()
                .persist(areaRepository);

        String otherDomain = uniqueDomain("other");
        SingularEntity otherSingular = SingularTestBuilder.forFederation(authProperties.defaultFederationId())
                .dominioEmail(otherDomain)
                .persist(singularRepository);
        AreaTestBuilder.forSingular(otherSingular.getId())
                .nome("Área Alheia")
                .persist(areaRepository);

        mockMvc.perform(get("/api/v1/auth/primeiro-acesso/areas").cookie(context.accessCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].id").value(ownActive.getId().intValue()))
                .andExpect(jsonPath("$.data[0].name").value("Área Própria"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-PA-003", type = AcceptanceCriterion.TestType.API)
    void shouldRejectOperationalAreaListForPrimeiroAcessoJwt() throws Exception {
        PaContext context = seedMappedPrimeiroAcesso();
        AreaTestBuilder.forSingular(context.singular().getId()).persist(areaRepository);

        mockMvc.perform(get("/api/v1/areas").cookie(context.accessCookie()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("FORBIDDEN"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-PA-003", type = AcceptanceCriterion.TestType.API)
    void shouldRejectPrimeiroAcessoAreaListForOperationalJwt() throws Exception {
        String email = IntegrationTestUniqueData.colaboradorEmail("op-list");
        ColaboradorTestBuilder.forFederation(authProperties.defaultFederationId())
                .email(email)
                .nome("Operacional")
                .zimbraId("zimbra-" + email)
                .persist(colaboradorRepository);
        Cookie operational = operationalCookieForEmail(email);

        mockMvc.perform(get("/api/v1/auth/primeiro-acesso/areas").cookie(operational))
                .andExpect(status().isForbidden());
    }

    @Test
    @AcceptanceCriterion(value = "AT-PA-003", type = AcceptanceCriterion.TestType.API)
    void shouldRejectAreaListWhenDomainHasNoSingular() throws Exception {
        Cookie accessCookie = primeiroAcessoCookieForEmail("user@dominio-inexistente.test");

        mockMvc.perform(get("/api/v1/auth/primeiro-acesso/areas").cookie(accessCookie))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value(SecurityConstants.PA_DOMAIN_NO_SINGULAR));
    }

    @Test
    @AcceptanceCriterion(value = "AT-PA-011", type = AcceptanceCriterion.TestType.API)
    void shouldCompletePrimeiroAcessoAndIssueOperationalSession() throws Exception {
        PaContext context = seedMappedPrimeiroAcesso();
        AreaEntity area = AreaTestBuilder.forSingular(context.singular().getId())
                .nome("Onboarding")
                .persist(areaRepository);
        Cookie csrfCookie = obtainCsrfCookie();
        long sessionsBefore = authSessaoRepository.count();

        MvcResult result = mockMvc.perform(post("/api/v1/auth/primeiro-acesso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(context.accessCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                { "areaId": %d }
                                """.formatted(area.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.primeiroAcesso").value(false))
                .andExpect(jsonPath("$.data.organizationalLinks.singularId")
                        .value(context.singular().getId().intValue()))
                .andExpect(jsonPath("$.data.organizationalLinks.areaId").value(area.getId().intValue()))
                .andExpect(jsonPath("$.data.organizationalLinks.federationId")
                        .value(context.singular().getFederacaoId().intValue()))
                .andReturn();

        Cookie operationalAccess = findCookie(result, SecurityConstants.ACCESS_TOKEN_COOKIE);
        Cookie operationalRefresh = findCookie(result, SecurityConstants.REFRESH_TOKEN_COOKIE);
        assertThat(operationalAccess).isNotNull();
        assertThat(operationalRefresh).isNotNull();
        assertThat(authSessaoRepository.count()).isEqualTo(sessionsBefore + 1);

        Optional<ColaboradorEntity> created = colaboradorRepository.findByEmailIgnoreCase(context.email());
        assertThat(created).isPresent();
        assertThat(created.get().getAreaId()).isEqualTo(area.getId());
        assertThat(created.get().getSingularId()).isEqualTo(context.singular().getId());

        mockMvc.perform(get("/api/v1/auth/me").cookie(operationalAccess))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.primeiroAcesso").value(false))
                .andExpect(jsonPath("$.data.id").value(created.get().getId().intValue()));
    }

    @Test
    @AcceptanceCriterion(value = "AT-PA-011", type = AcceptanceCriterion.TestType.API)
    void shouldRejectAreaFromAnotherSingular() throws Exception {
        PaContext context = seedMappedPrimeiroAcesso();
        String otherDomain = uniqueDomain("otherpa");
        SingularEntity otherSingular = SingularTestBuilder.forFederation(authProperties.defaultFederationId())
                .dominioEmail(otherDomain)
                .persist(singularRepository);
        AreaEntity foreignArea = AreaTestBuilder.forSingular(otherSingular.getId())
                .nome("Estrangeira")
                .persist(areaRepository);
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/auth/primeiro-acesso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(context.accessCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                { "areaId": %d }
                                """.formatted(foreignArea.getId())))
                .andExpect(status().isUnprocessableEntity());

        assertThat(colaboradorRepository.findByEmailIgnoreCase(context.email())).isEmpty();
    }

    @Test
    @AcceptanceCriterion(value = "AT-PA-011", type = AcceptanceCriterion.TestType.API)
    void shouldRejectInactiveArea() throws Exception {
        PaContext context = seedMappedPrimeiroAcesso();
        AreaEntity inactive = AreaTestBuilder.forSingular(context.singular().getId())
                .nome("Inativa")
                .inativa()
                .persist(areaRepository);
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/auth/primeiro-acesso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(context.accessCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                { "areaId": %d }
                                """.formatted(inactive.getId())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @AcceptanceCriterion(value = "AT-PA-011", type = AcceptanceCriterion.TestType.API)
    void shouldConflictWhenColaboradorAlreadyExists() throws Exception {
        PaContext context = seedMappedPrimeiroAcesso();
        AreaEntity area = AreaTestBuilder.forSingular(context.singular().getId()).persist(areaRepository);
        ColaboradorTestBuilder.forFederation(authProperties.defaultFederationId())
                .email(context.email())
                .zimbraId("zimbra-pa-" + context.email())
                .persist(colaboradorRepository);
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/auth/primeiro-acesso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(context.accessCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                { "areaId": %d }
                                """.formatted(area.getId())))
                .andExpect(status().isConflict());
    }

    @Test
    @AcceptanceCriterion(value = "AT-PA-011", type = AcceptanceCriterion.TestType.API)
    void shouldRejectCompletionForOperationalJwt() throws Exception {
        String email = IntegrationTestUniqueData.colaboradorEmail("op-complete");
        ColaboradorTestBuilder.forFederation(authProperties.defaultFederationId())
                .email(email)
                .nome("Operacional")
                .zimbraId("zimbra-" + email)
                .persist(colaboradorRepository);
        Cookie operational = operationalCookieForEmail(email);
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/auth/primeiro-acesso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(operational, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{ \"areaId\": 1 }"))
                .andExpect(status().isForbidden());
    }

    private PaContext seedMappedPrimeiroAcesso() throws Exception {
        String domain = uniqueDomain("pa");
        SingularEntity singular = SingularTestBuilder.forFederation(authProperties.defaultFederationId())
                .dominioEmail(domain)
                .persist(singularRepository);
        String email = "user@" + domain;
        Cookie accessCookie = primeiroAcessoCookieForEmail(email);
        return new PaContext(email, singular, accessCookie);
    }

    private String uniqueDomain(String prefix) {
        return prefix + "-" + IntegrationTestUniqueData.singularSigla("dm").toLowerCase() + ".test";
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

    private Cookie operationalCookieForEmail(String email) throws Exception {
        testIdentityProviderClient().setValidationBehavior(token -> {
            if (TestIdentityProviderClient.VALID_TOKEN.equals(token)) {
                return new IdentityValidationResult(email, "Operacional", "zimbra-" + email);
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

    private Cookie findCookie(MvcResult result, String name) {
        if (result.getResponse().getCookies() != null) {
            for (Cookie cookie : result.getResponse().getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        Collection<String> setCookies = result.getResponse().getHeaders("Set-Cookie");
        return setCookies.stream()
                .filter(header -> header.startsWith(name + "="))
                .map(header -> {
                    String value = header.substring(name.length() + 1).split(";", 2)[0];
                    return new Cookie(name, value);
                })
                .findFirst()
                .orElse(null);
    }

    private TestIdentityProviderClient testIdentityProviderClient() {
        return (TestIdentityProviderClient) identityProviderClient;
    }

    private record PaContext(String email, SingularEntity singular, Cookie accessCookie) {
    }
}
