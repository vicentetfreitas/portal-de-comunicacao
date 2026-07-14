package br.com.unimedceara.portalcomunicacao.organization.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.security.TestSecurityContextFactory;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Fluxo cross-feature: Singular → Área → Equipe → Colaborador (xft-org-01).
 */
@IntegrationTest
class OrgCrossFeatureIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private AuthProperties authProperties;

    private MockMvc mockMvc;
    private JsonMapper jsonMapper = JsonMapper.builder().build();

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        ensureAdmin();
    }

    @Test
    @AcceptanceCriterion(value = "XFT-ORG-01", type = AcceptanceCriterion.TestType.API)
    void shouldExecuteFullOrganizationalHierarchy() throws Exception {
        Cookie accessCookie = adminCookie();
        Cookie csrfCookie = obtainCsrfCookie();

        long singularId = createSingular(accessCookie, csrfCookie);
        long areaId = createArea(accessCookie, csrfCookie, singularId);
        long equipeId = createEquipe(accessCookie, csrfCookie, areaId);
        long colaboradorId = createColaborador(accessCookie, csrfCookie, singularId, areaId, equipeId);

        mockMvc.perform(get("/api/v1/colaboradores/" + colaboradorId)
                        .param("teamId", String.valueOf(equipeId))
                        .cookie(accessCookie))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/colaboradores")
                        .param("teamId", String.valueOf(equipeId))
                        .cookie(accessCookie))
                .andExpect(status().isOk());
    }

    private long createSingular(Cookie accessCookie, Cookie csrfCookie) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/singulares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federationId": %d,
                                  "name": "Singular Integração",
                                  "acronym": "SI",
                                  "unimedCode": "777"
                                }
                                """.formatted(authProperties.defaultFederationId())))
                .andExpect(status().isCreated())
                .andReturn();
        return readId(result);
    }

    private long createArea(Cookie accessCookie, Cookie csrfCookie, long singularId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/areas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "singularId": %d,
                                  "name": "Área Integração"
                                }
                                """.formatted(singularId)))
                .andExpect(status().isCreated())
                .andReturn();
        return readId(result);
    }

    private long createEquipe(Cookie accessCookie, Cookie csrfCookie, long areaId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/equipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "areaId": %d,
                                  "name": "Equipe Integração"
                                }
                                """.formatted(areaId)))
                .andExpect(status().isCreated())
                .andReturn();
        return readId(result);
    }

    private long createColaborador(
            Cookie accessCookie, Cookie csrfCookie, long singularId, long areaId, long equipeId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/colaboradores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federationId": %d,
                                  "singularId": %d,
                                  "areaId": %d,
                                  "teamId": %d,
                                  "name": "Colaborador Integração",
                                  "email": "integracao@unimedceara.com.br"
                                }
                                """
                                .formatted(
                                        authProperties.defaultFederationId(), singularId, areaId, equipeId)))
                .andExpect(status().isCreated())
                .andReturn();
        long id = readId(result);
        assertThat(id).isPositive();
        return id;
    }

    private long readId(MvcResult result) throws Exception {
        JsonNode root = jsonMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("id").asLong();
    }

    private void ensureAdmin() {
        colaboradorRepository
                .findByEmailIgnoreCase("colaborador@unimedceara.com.br")
                .orElseGet(() -> {
                    ColaboradorEntity admin = new ColaboradorEntity();
                    admin.setEmail("colaborador@unimedceara.com.br");
                    admin.setNome("Admin");
                    admin.setZimbraId("zimbra-admin");
                    admin.setAtivo("S");
                    admin.setFederacaoId(authProperties.defaultFederationId());
                    admin.setDataCadastro(Instant.now());
                    return colaboradorRepository.save(admin);
                });
    }

    private Cookie adminCookie() {
        ColaboradorEntity admin = colaboradorRepository
                .findByEmailIgnoreCase("colaborador@unimedceara.com.br")
                .orElseThrow();
        return TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
    }

    private Cookie obtainCsrfCookie() throws Exception {
        return mockMvc.perform(get("/actuator/health"))
                .andReturn()
                .getResponse()
                .getCookie(SecurityConstants.CSRF_COOKIE);
    }
}
