package br.com.unimedceara.portalcomunicacao.organization.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.FederacaoStatus;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.FederacaoEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.FederacaoRepository;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.ColaboradorTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.security.TestSecurityContextFactory;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@IntegrationTest
class FederacaoAcceptanceIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private FederacaoRepository federacaoRepository;

    @Autowired
    private AuthProperties authProperties;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    void shouldCreateFederacao() throws Exception {
        Cookie accessCookie = adminCookie();
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/federacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "name": "Federação Teste",
                                  "acronym": "FED-T",
                                  "unimedCode": 980,
                                  "ansRegistration": "99999-9"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Federação Teste"))
                .andExpect(jsonPath("$.data.unimedCode").value(980));
    }

    @Test
    void shouldFindFederacaoById() throws Exception {
        FederacaoEntity federacao = seedFederacao("FED-F", 981, "88888-8");
        mockMvc.perform(get("/api/v1/federacoes/" + federacao.getId()).cookie(adminCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.acronym").value("FED-F"));
    }

    @Test
    void shouldListFederacoes() throws Exception {
        seedFederacao("FED-L", 982, "77777-7");
        mockMvc.perform(get("/api/v1/federacoes").cookie(adminCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").isNumber());
    }

    @Test
    void shouldUpdateFederacao() throws Exception {
        FederacaoEntity federacao = seedFederacao("FED-U", 983, "66666-6");
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(put("/api/v1/federacoes/" + federacao.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "name": "Nome Atualizado",
                                  "acronym": "FED-U",
                                  "unimedCode": 983,
                                  "ansRegistration": "66666-6"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Nome Atualizado"));
    }

    @Test
    void shouldReturn401WhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/federacoes/1")).andExpect(status().isUnauthorized());
    }

    private FederacaoEntity seedFederacao(String acronym, int unimedCode, String ansRegistration) {
        FederacaoEntity federacao = new FederacaoEntity();
        federacao.setNome("Federação Seed");
        federacao.setSigla(acronym);
        federacao.setCodigoUnimed(unimedCode);
        federacao.setRegistroAns(ansRegistration);
        federacao.setAtivo(FederacaoStatus.ACTIVE.toFlag());
        federacao.setDataCadastro(Instant.now());
        return federacaoRepository.save(federacao);
    }

    private Cookie adminCookie() {
        return TestSecurityContextFactory.jwtCookie(jwtTokenService, ensureAdmin().getId());
    }

    private ColaboradorEntity ensureAdmin() {
        return colaboradorRepository
                .findByEmailIgnoreCase(ColaboradorTestBuilder.SESSION_ADMINISTRATOR_EMAIL)
                .orElseGet(() -> {
                    ColaboradorEntity c = new ColaboradorEntity();
                    c.setEmail(ColaboradorTestBuilder.SESSION_ADMINISTRATOR_EMAIL);
                    c.setNome("Admin");
                    c.setZimbraId("zimbra-" + ColaboradorTestBuilder.SESSION_ADMINISTRATOR_EMAIL);
                    c.setAtivo(SingularStatus.ACTIVE.toFlag());
                    c.setFederacaoId(authProperties.defaultFederationId());
                    c.setDataCadastro(Instant.now());
                    return colaboradorRepository.save(c);
                });
    }

    private Cookie obtainCsrfCookie() throws Exception {
        return mockMvc.perform(get("/actuator/health"))
                .andReturn()
                .getResponse()
                .getCookie(SecurityConstants.CSRF_COOKIE);
    }
}
