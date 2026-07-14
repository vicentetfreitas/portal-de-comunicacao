package br.com.unimedceara.portalcomunicacao.organization.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.EquipeEntity;
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

/**
 * Suíte de testes automatizados dos critérios de aceite FT-AREA.
 */
@IntegrationTest
class AreaAcceptanceIntegrationTest {

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
    @AcceptanceCriterion(value = "AT-AREA-001", type = AcceptanceCriterion.TestType.API)
    void atArea001_shouldCreateAreaSuccessfully() throws Exception {
        SingularEntity singular = seedActiveSingular();
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/areas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
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
        ColaboradorEntity admin = ensureAdminColaborador();
        seedArea(singular.getId(), "Financeiro", null);
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
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
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-001", type = AcceptanceCriterion.TestType.API)
    void atArea001_shouldRejectUnauthorizedCreate() throws Exception {
        SingularEntity singular = seedActiveSingular();
        ColaboradorEntity regular = ensureRegularColaborador();
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
        AreaEntity area = seedArea(singular.getId(), "Financeiro", null);
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/areas/" + area.getId()).cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(area.getId().intValue()))
                .andExpect(jsonPath("$.data.name").value("Financeiro"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-002", type = AcceptanceCriterion.TestType.API)
    void atArea002_shouldReturn404WhenNotFound() throws Exception {
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/areas/999999").cookie(accessCookie))
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
        seedArea(singular.getId(), "Financeiro", null);
        seedArea(singular.getId(), "RH", null);
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/areas")
                        .param("singularId", String.valueOf(singular.getId()))
                        .param("status", "ACTIVE")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name,asc")
                        .cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-003", type = AcceptanceCriterion.TestType.API)
    void atArea003_shouldReturnEmptyList() throws Exception {
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/areas").param("name", "INEXISTENTE").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isEmpty());
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-004", type = AcceptanceCriterion.TestType.API)
    void atArea004_shouldUpdateArea() throws Exception {
        SingularEntity singular = seedActiveSingular();
        AreaEntity area = seedArea(singular.getId(), "Financeiro", null);
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(put("/api/v1/areas/" + area.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
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
    @AcceptanceCriterion(value = "AT-AREA-004", type = AcceptanceCriterion.TestType.API)
    void atArea004_shouldRejectHierarchyCycle() throws Exception {
        SingularEntity singular = seedActiveSingular();
        AreaEntity parent = seedArea(singular.getId(), "Pai", null);
        AreaEntity child = seedArea(singular.getId(), "Filha", parent.getId());
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(put("/api/v1/areas/" + parent.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "parentAreaId": %d,
                                  "name": "Pai"
                                }
                                """.formatted(child.getId())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-005", type = AcceptanceCriterion.TestType.API)
    void atArea005_shouldInactivateArea() throws Exception {
        SingularEntity singular = seedActiveSingular();
        AreaEntity area = seedArea(singular.getId(), "Financeiro", null);
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/areas/" + area.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"INACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("INACTIVE"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-005", type = AcceptanceCriterion.TestType.API)
    void atArea005_shouldBlockInactivationWithActiveChild() throws Exception {
        SingularEntity singular = seedActiveSingular();
        AreaEntity parent = seedArea(singular.getId(), "Pai", null);
        seedArea(singular.getId(), "Filha", parent.getId());
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/areas/" + parent.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"INACTIVE\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @AcceptanceCriterion(value = "AT-AREA-005", type = AcceptanceCriterion.TestType.API)
    void atArea005_shouldReactivateArea() throws Exception {
        SingularEntity singular = seedActiveSingular();
        AreaEntity area = seedArea(singular.getId(), "Financeiro", null);
        area.setAtivo(AreaStatus.INACTIVE.toFlag());
        areaRepository.save(area);

        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/areas/" + area.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"ACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    private SingularEntity seedActiveSingular() {
        SingularEntity singular = new SingularEntity();
        singular.setFederacaoId(authProperties.defaultFederationId());
        singular.setNome("Singular Teste");
        singular.setSigla("ST");
        singular.setCodigoUnimed("001");
        singular.setAtivo(AreaStatus.ACTIVE.toFlag());
        singular.setDataCadastro(Instant.now());
        return singularRepository.save(singular);
    }

    private AreaEntity seedArea(Long singularId, String name, Long parentAreaId) {
        AreaEntity area = new AreaEntity();
        area.setSingularId(singularId);
        area.setParentAreaId(parentAreaId);
        area.setNome(name);
        area.setAtivo(AreaStatus.ACTIVE.toFlag());
        area.setDataCadastro(Instant.now());
        return areaRepository.save(area);
    }

    private ColaboradorEntity ensureAdminColaborador() {
        return colaboradorRepository.findByEmailIgnoreCase("colaborador@unimedceara.com.br")
                .orElseGet(() -> {
                    ColaboradorEntity colaborador = new ColaboradorEntity();
                    colaborador.setEmail("colaborador@unimedceara.com.br");
                    colaborador.setNome("Admin Teste");
                    colaborador.setZimbraId("zimbra-admin");
                    colaborador.setAtivo(AreaStatus.ACTIVE.toFlag());
                    colaborador.setFederacaoId(authProperties.defaultFederationId());
                    colaborador.setDataCadastro(Instant.now());
                    return colaboradorRepository.save(colaborador);
                });
    }

    private ColaboradorEntity ensureRegularColaborador() {
        return colaboradorRepository.findByEmailIgnoreCase("regular@unimedceara.com.br")
                .orElseGet(() -> {
                    ColaboradorEntity colaborador = new ColaboradorEntity();
                    colaborador.setEmail("regular@unimedceara.com.br");
                    colaborador.setNome("Regular Teste");
                    colaborador.setZimbraId("zimbra-regular");
                    colaborador.setAtivo(AreaStatus.ACTIVE.toFlag());
                    colaborador.setFederacaoId(authProperties.defaultFederationId());
                    colaborador.setDataCadastro(Instant.now());
                    return colaboradorRepository.save(colaborador);
                });
    }

    private Cookie obtainCsrfCookie() throws Exception {
        return mockMvc.perform(get("/actuator/health"))
                .andReturn()
                .getResponse()
                .getCookie(SecurityConstants.CSRF_COOKIE);
    }
}
