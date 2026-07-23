package br.com.unimedceara.portalcomunicacao.organization.acceptance;

import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.base.AbstractMockMvcIntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.data.IntegrationTestUniqueData;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Fluxo cross-feature: Singular → Área → Equipe → Colaborador (xft-org-01).
 */
@IntegrationTest
class OrgCrossFeatureIntegrationTest extends AbstractMockMvcIntegrationTest {

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Test
    @AcceptanceCriterion(value = "XFT-ORG-01", type = AcceptanceCriterion.TestType.API)
    void shouldExecuteFullOrganizationalHierarchy() throws Exception {
        Cookie accessCookie = adminCookie();
        Cookie csrfCookie = obtainCsrfCookie();

        long singularId = createSingular(accessCookie, csrfCookie);
        long areaId = createArea(accessCookie, csrfCookie, singularId);
        long equipeId = createEquipe(accessCookie, csrfCookie, areaId);
        String colaboradorEmail = IntegrationTestUniqueData.colaboradorEmail("integracao");
        String zimbraId = "zimbra-" + colaboradorEmail.replace("@", "-");
        long colaboradorId =
                createColaborador(accessCookie, csrfCookie, singularId, areaId, equipeId, colaboradorEmail, zimbraId);

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
        String acronym = IntegrationTestUniqueData.singularSigla("SI");
        int unimedCode = IntegrationTestUniqueData.singularUnimedCode();
        String registroAns = IntegrationTestUniqueData.registroAnsForUnimedCode(unimedCode);
        MvcResult result = mockMvc.perform(post("/api/v1/singulares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federationId": %d,
                                  "name": "Singular Integração",
                                  "acronym": "%s",
                                  "unimedCode": %d,
                                  "registroAns": "%s"
                                }
                                """
                                .formatted(authProperties.defaultFederationId(), acronym, unimedCode, registroAns)))
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
            Cookie accessCookie,
            Cookie csrfCookie,
            long singularId,
            long areaId,
            long equipeId,
            String email,
            String zimbraId) throws Exception {
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
                                  "email": "%s",
                                  "zimbraId": "%s"
                                }
                                """
                                .formatted(
                                        authProperties.defaultFederationId(),
                                        singularId,
                                        areaId,
                                        equipeId,
                                        email,
                                        zimbraId)))
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
}
