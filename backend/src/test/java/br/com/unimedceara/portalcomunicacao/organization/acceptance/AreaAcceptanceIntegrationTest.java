package br.com.unimedceara.portalcomunicacao.organization.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.base.AbstractMockMvcIntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.AreaTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.ColaboradorTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.SingularTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.security.TestSecurityContextFactory;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Suíte de testes automatizados dos critérios de aceite FT-AREA.
 */
@IntegrationTest
@Tag("integration.mutating")
class AreaAcceptanceIntegrationTest extends AbstractMockMvcIntegrationTest {

    @Autowired
    private SingularRepository singularRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Test
    @AcceptanceCriterion(value = "AT-AREA-001", type = AcceptanceCriterion.TestType.API)
    void atArea001_shouldCreateAreaSuccessfully() throws Exception {
        SingularEntity singular = seedActiveSingular();
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/areas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "singularId": %d,
                                  "name": "Financeiro",
                                  "acronym": "FIN"
                                }
                                """.formatted(singular.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Financeiro"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"))
                .andExpect(jsonPath("$.data.singularId").value(singular.getId().intValue()));
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-001", type = AcceptanceCriterion.TestType.API)
    void atArea001_shouldRejectDuplicateName() throws Exception {
        SingularEntity singular = seedActiveSingular();
        seedArea(singular.getId(), "Financeiro");
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/areas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "singularId": %d,
                                  "name": "Financeiro"
                                }
                                """.formatted(singular.getId())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-001", type = AcceptanceCriterion.TestType.API)
    void atArea001_shouldRejectUnauthorizedCreate() throws Exception {
        SingularEntity singular = seedActiveSingular();
        ColaboradorEntity regular = ColaboradorTestBuilder.forFederation(authProperties.defaultFederationId())
                .email("regular@unimedceara.com.br")
                .nome("Regular Teste")
                .zimbraId("zimbra-regular")
                .persist(colaboradorRepository);
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, regular.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/areas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "singularId": %d,
                                  "name": "Financeiro"
                                }
                                """.formatted(singular.getId())))
                .andExpect(status().isForbidden());
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-002", type = AcceptanceCriterion.TestType.API)
    void atArea002_shouldFindAreaById() throws Exception {
        SingularEntity singular = seedActiveSingular();
        AreaEntity area = seedArea(singular.getId(), "Financeiro");

        mockMvc.perform(get("/api/v1/areas/" + area.getId()).cookie(adminCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(area.getId().intValue()))
                .andExpect(jsonPath("$.data.name").value("Financeiro"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-002", type = AcceptanceCriterion.TestType.API)
    void atArea002_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/areas/999999").cookie(adminCookie()))
                .andExpect(status().isNotFound());
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-002", type = AcceptanceCriterion.TestType.API)
    void atArea002_shouldReturn401WhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/areas/1")).andExpect(status().isUnauthorized());
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-003", type = AcceptanceCriterion.TestType.API)
    void atArea003_shouldListAreasWithFilters() throws Exception {
        SingularEntity singular = seedActiveSingular();
        seedArea(singular.getId(), "Financeiro");
        seedArea(singular.getId(), "RH");

        mockMvc.perform(get("/api/v1/areas")
                        .param("singularId", String.valueOf(singular.getId()))
                        .param("status", "ACTIVE")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name,asc")
                        .cookie(adminCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-003", type = AcceptanceCriterion.TestType.API)
    void atArea003_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/v1/areas").param("name", "INEXISTENTE").cookie(adminCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isEmpty());
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-004", type = AcceptanceCriterion.TestType.API)
    void atArea004_shouldUpdateArea() throws Exception {
        SingularEntity singular = seedActiveSingular();
        AreaEntity area = seedArea(singular.getId(), "Financeiro");
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(put("/api/v1/areas/" + area.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "name": "Financeiro e Controladoria",
                                  "acronym": "FIN"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Financeiro e Controladoria"))
                .andExpect(jsonPath("$.data.updatedAt").exists());
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-005", type = AcceptanceCriterion.TestType.API)
    void atArea005_shouldInactivateArea() throws Exception {
        SingularEntity singular = seedActiveSingular();
        AreaEntity area = seedArea(singular.getId(), "Financeiro");
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/areas/" + area.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"INACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("INACTIVE"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-005", type = AcceptanceCriterion.TestType.API)
    void atArea005_shouldReactivateArea() throws Exception {
        SingularEntity singular = seedActiveSingular();
        AreaEntity area = seedArea(singular.getId(), "Financeiro");
        area.setAtivo(AreaStatus.INACTIVE.toFlag());
        areaRepository.save(area);

        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/areas/" + area.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"ACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    private SingularEntity seedActiveSingular() {
        return SingularTestBuilder.forFederation(authProperties.defaultFederationId())
                .persist(singularRepository);
    }

    private AreaEntity seedArea(Long singularId, String name) {
        return AreaTestBuilder.forSingular(singularId).nome(name).persist(areaRepository);
    }
}
