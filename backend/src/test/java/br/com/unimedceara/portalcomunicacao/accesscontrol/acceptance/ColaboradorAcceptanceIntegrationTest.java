package br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.EquipeRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.base.AbstractMockMvcIntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.fixture.OrganizationalTestFixtures;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.ColaboradorTestBuilder;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ColaboradorAcceptanceIntegrationTest extends AbstractMockMvcIntegrationTest {

    @Autowired
    private SingularRepository singularRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    private OrganizationalTestFixtures.Hierarchy hierarchy;

    @BeforeEach
    void seedHierarchy() {
        hierarchy = OrganizationalTestFixtures.persistMinimalHierarchy(
                authProperties.defaultFederationId(), singularRepository, areaRepository, equipeRepository);
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-001", type = AcceptanceCriterion.TestType.API)
    void shouldCreateColaborador() throws Exception {
        Cookie csrfCookie = obtainCsrfCookie();
        mockMvc.perform(post("/api/v1/colaboradores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federationId": %d,
                                  "singularId": %d,
                                  "areaId": %d,
                                  "teamId": %d,
                                  "name": "João Silva",
                                  "email": "joao@unimedceara.com.br",
                                  "zimbraId": "zimbra-joao"
                                }
                                """
                                .formatted(
                                        authProperties.defaultFederationId(),
                                        hierarchy.singularId(),
                                        hierarchy.areaId(),
                                        hierarchy.equipeId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("João Silva"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-002", type = AcceptanceCriterion.TestType.API)
    void shouldFindColaboradorById() throws Exception {
        ColaboradorEntity colaborador = seedColaborador("maria@unimedceara.com.br");
        mockMvc.perform(get("/api/v1/colaboradores/" + colaborador.getId()).cookie(adminCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("maria@unimedceara.com.br"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-003", type = AcceptanceCriterion.TestType.API)
    void shouldListColaboradoresByTeam() throws Exception {
        seedColaborador("lista@unimedceara.com.br");
        mockMvc.perform(get("/api/v1/colaboradores")
                        .param("teamId", String.valueOf(hierarchy.equipeId()))
                        .cookie(adminCookie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-004", type = AcceptanceCriterion.TestType.API)
    void shouldUpdateColaborador() throws Exception {
        ColaboradorEntity colaborador = seedColaborador("update@unimedceara.com.br");
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(put("/api/v1/colaboradores/" + colaborador.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "singularId": %d,
                                  "areaId": %d,
                                  "teamId": %d,
                                  "name": "Nome Atualizado",
                                  "zimbraId": "zimbra-update"
                                }
                                """
                                .formatted(hierarchy.singularId(), hierarchy.areaId(), hierarchy.equipeId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Nome Atualizado"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-001", type = AcceptanceCriterion.TestType.API)
    void shouldRejectInvalidTeamForArea() throws Exception {
        Cookie csrfCookie = obtainCsrfCookie();
        mockMvc.perform(post("/api/v1/colaboradores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federationId": %d,
                                  "areaId": %d,
                                  "teamId": 999999,
                                  "name": "Inválido",
                                  "email": "invalido@unimedceara.com.br",
                                  "zimbraId": "zimbra-invalido"
                                }
                                """
                                .formatted(authProperties.defaultFederationId(), hierarchy.areaId())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {
        seedColaborador("dup@unimedceara.com.br");
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/colaboradores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federationId": %d,
                                  "teamId": %d,
                                  "name": "Outro",
                                  "email": "dup@unimedceara.com.br",
                                  "zimbraId": "zimbra-dup-2"
                                }
                                """
                                .formatted(authProperties.defaultFederationId(), hierarchy.equipeId())))
                .andExpect(status().isUnprocessableEntity());
    }

    private ColaboradorEntity seedColaborador(String email) {
        return ColaboradorTestBuilder.forFederation(authProperties.defaultFederationId())
                .singularId(hierarchy.singularId())
                .areaId(hierarchy.areaId())
                .equipeId(hierarchy.equipeId())
                .email(email)
                .zimbraId("zimbra-" + email.replace("@", "-"))
                .persist(colaboradorRepository);
    }
}
