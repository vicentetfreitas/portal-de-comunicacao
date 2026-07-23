package br.com.unimedceara.portalcomunicacao.organization.acceptance;

import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.base.AbstractMockMvcIntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.data.IntegrationTestUniqueData;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.SingularTestBuilder;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class SingularAcceptanceIntegrationTest extends AbstractMockMvcIntegrationTest {

    @Autowired
    private SingularRepository singularRepository;

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-001", type = AcceptanceCriterion.TestType.API)
    void shouldCreateSingular() throws Exception {
        Cookie csrfCookie = obtainCsrfCookie();
        String acronym = IntegrationTestUniqueData.singularSigla("SN");
        int unimedCode = IntegrationTestUniqueData.singularUnimedCode();
        String registroAns = IntegrationTestUniqueData.registroAnsForUnimedCode(unimedCode);

        mockMvc.perform(post("/api/v1/singulares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federationId": %d,
                                  "name": "Singular Norte",
                                  "acronym": "%s",
                                  "unimedCode": %d,
                                  "registroAns": "%s"
                                }
                                """
                                .formatted(
                                        authProperties.defaultFederationId(), acronym, unimedCode, registroAns)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Singular Norte"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-002", type = AcceptanceCriterion.TestType.API)
    void shouldFindSingularById() throws Exception {
        SingularEntity singular = seedSingular();
        mockMvc.perform(get("/api/v1/singulares/" + singular.getId()).cookie(adminCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(singular.getId().intValue()));
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-003", type = AcceptanceCriterion.TestType.API)
    void shouldListSingulares() throws Exception {
        SingularEntity singular = seedSingular();
        mockMvc.perform(get("/api/v1/singulares")
                        .param("acronym", singular.getSigla())
                        .param("page", "0")
                        .param("size", "10")
                        .cookie(adminCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-004", type = AcceptanceCriterion.TestType.API)
    void shouldUpdateSingular() throws Exception {
        SingularEntity singular = seedSingular();
        Cookie csrfCookie = obtainCsrfCookie();
        String newAcronym = IntegrationTestUniqueData.singularSigla("SA");
        int newUnimedCode = IntegrationTestUniqueData.singularUnimedCode();
        String newRegistroAns = IntegrationTestUniqueData.registroAnsForUnimedCode(newUnimedCode);

        mockMvc.perform(put("/api/v1/singulares/" + singular.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "name": "Singular Atualizada",
                                  "acronym": "%s",
                                  "unimedCode": %d,
                                  "registroAns": "%s"
                                }
                                """
                                .formatted(newAcronym, newUnimedCode, newRegistroAns)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Singular Atualizada"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-005", type = AcceptanceCriterion.TestType.API)
    void shouldInactivateSingular() throws Exception {
        SingularEntity singular = seedSingular();
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
        String acronym = IntegrationTestUniqueData.singularSigla("DP");
        int existingUnimed = IntegrationTestUniqueData.singularUnimedCode();
        seedSingular(acronym, existingUnimed);
        int duplicateUnimed = IntegrationTestUniqueData.singularUnimedCode();
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/singulares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federationId": %d,
                                  "name": "Outra",
                                  "acronym": "%s",
                                  "unimedCode": %d,
                                  "registroAns": "%s"
                                }
                                """
                                .formatted(
                                        authProperties.defaultFederationId(),
                                        acronym,
                                        duplicateUnimed,
                                        IntegrationTestUniqueData.registroAnsForUnimedCode(duplicateUnimed))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldReturn401WhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/singulares/1")).andExpect(status().isUnauthorized());
    }

    private SingularEntity seedSingular() {
        int unimedCode = IntegrationTestUniqueData.singularUnimedCode();
        return seedSingular(IntegrationTestUniqueData.singularSigla("SN"), unimedCode);
    }

    private SingularEntity seedSingular(String acronym, int unimedCode) {
        return SingularTestBuilder.forFederation(authProperties.defaultFederationId())
                .sigla(acronym)
                .codigoUnimed(unimedCode)
                .persist(singularRepository);
    }
}
