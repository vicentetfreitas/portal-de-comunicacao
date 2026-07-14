package br.com.unimedceara.portalcomunicacao.organization.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.EquipeRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
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
class SingularAcceptanceIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private SingularRepository singularRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private AuthProperties authProperties;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        areaRepository.deleteAll();
        equipeRepository.deleteAll();
        singularRepository.deleteAll();
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-001", type = AcceptanceCriterion.TestType.API)
    void shouldCreateSingular() throws Exception {
        Cookie accessCookie = adminCookie();
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/singulares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federationId": %d,
                                  "name": "Singular Norte",
                                  "acronym": "SN",
                                  "unimedCode": "002"
                                }
                                """.formatted(authProperties.defaultFederationId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Singular Norte"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-002", type = AcceptanceCriterion.TestType.API)
    void shouldFindSingularById() throws Exception {
        SingularEntity singular = seedSingular("SN", "002");
        mockMvc.perform(get("/api/v1/singulares/" + singular.getId()).cookie(adminCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(singular.getId().intValue()));
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-003", type = AcceptanceCriterion.TestType.API)
    void shouldListSingulares() throws Exception {
        seedSingular("SN", "002");
        mockMvc.perform(get("/api/v1/singulares")
                        .param("page", "0")
                        .param("size", "10")
                        .cookie(adminCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-004", type = AcceptanceCriterion.TestType.API)
    void shouldUpdateSingular() throws Exception {
        SingularEntity singular = seedSingular("SN", "002");
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(put("/api/v1/singulares/" + singular.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "name": "Singular Atualizada",
                                  "acronym": "SA",
                                  "unimedCode": "003"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Singular Atualizada"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-005", type = AcceptanceCriterion.TestType.API)
    void shouldInactivateSingular() throws Exception {
        SingularEntity singular = seedSingular("SN", "002");
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/singulares/" + singular.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"INACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("INACTIVE"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-001", type = AcceptanceCriterion.TestType.API)
    void shouldRejectDuplicateAcronym() throws Exception {
        seedSingular("SN", "002");
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/singulares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federationId": %d,
                                  "name": "Outra",
                                  "acronym": "SN",
                                  "unimedCode": "099"
                                }
                                """.formatted(authProperties.defaultFederationId())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldReturn401WhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/singulares/1")).andExpect(status().isUnauthorized());
    }

    private SingularEntity seedSingular(String acronym, String unimedCode) {
        SingularEntity singular = new SingularEntity();
        singular.setFederacaoId(authProperties.defaultFederationId());
        singular.setNome("Singular Teste");
        singular.setSigla(acronym);
        singular.setCodigoUnimed(unimedCode);
        singular.setAtivo(SingularStatus.ACTIVE.toFlag());
        singular.setDataCadastro(Instant.now());
        return singularRepository.save(singular);
    }

    private Cookie adminCookie() {
        return TestSecurityContextFactory.jwtCookie(jwtTokenService, ensureAdmin().getId());
    }

    private ColaboradorEntity ensureAdmin() {
        return colaboradorRepository
                .findByEmailIgnoreCase("colaborador@unimedceara.com.br")
                .orElseGet(() -> {
                    ColaboradorEntity c = new ColaboradorEntity();
                    c.setEmail("colaborador@unimedceara.com.br");
                    c.setNome("Admin");
                    c.setZimbraId("zimbra-admin");
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
