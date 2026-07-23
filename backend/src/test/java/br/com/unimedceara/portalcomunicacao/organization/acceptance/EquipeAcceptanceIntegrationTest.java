package br.com.unimedceara.portalcomunicacao.organization.acceptance;

import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.EquipeEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.EquipeRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.base.AbstractMockMvcIntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.AreaTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.ColaboradorTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.EquipeTestBuilder;
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
class EquipeAcceptanceIntegrationTest extends AbstractMockMvcIntegrationTest {

    @Autowired
    private SingularRepository singularRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-001", type = AcceptanceCriterion.TestType.API)
    void shouldCreateEquipe() throws Exception {
        AreaEntity area = seedArea();
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/equipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "areaId": %d,
                                  "name": "Equipe Alpha"
                                }
                                """.formatted(area.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Equipe Alpha"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-002", type = AcceptanceCriterion.TestType.API)
    void shouldFindEquipeById() throws Exception {
        EquipeEntity equipe = seedEquipe(seedArea());
        mockMvc.perform(get("/api/v1/equipes/" + equipe.getId()).cookie(adminCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(equipe.getId().intValue()));
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-003", type = AcceptanceCriterion.TestType.API)
    void shouldListEquipes() throws Exception {
        AreaEntity area = seedArea();
        seedEquipe(area);
        mockMvc.perform(get("/api/v1/equipes")
                        .param("areaId", String.valueOf(area.getId()))
                        .cookie(adminCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-004", type = AcceptanceCriterion.TestType.API)
    void shouldUpdateEquipe() throws Exception {
        EquipeEntity equipe = seedEquipe(seedArea());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(put("/api/v1/equipes/" + equipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"name\": \"Equipe Beta\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Equipe Beta"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-005", type = AcceptanceCriterion.TestType.API)
    void shouldBlockInactivationWithActiveColaborador() throws Exception {
        AreaEntity area = seedArea();
        EquipeEntity equipe = seedEquipe(area);
        ColaboradorTestBuilder.forFederation(authProperties.defaultFederationId())
                .areaId(area.getId())
                .equipeId(equipe.getId())
                .nome("Membro")
                .email("membro@unimedceara.com.br")
                .zimbraId("zimbra-membro")
                .persist(colaboradorRepository);

        Cookie csrfCookie = obtainCsrfCookie();
        mockMvc.perform(patch("/api/v1/equipes/" + equipe.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"INACTIVE\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    private AreaEntity seedArea() {
        SingularEntity singular = SingularTestBuilder.forFederation(authProperties.defaultFederationId())
                .persist(singularRepository);
        return AreaTestBuilder.forSingular(singular.getId()).persist(areaRepository);
    }

    private EquipeEntity seedEquipe(AreaEntity area) {
        return EquipeTestBuilder.forArea(area.getId()).persist(equipeRepository);
    }
}
