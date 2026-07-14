package br.com.unimedceara.portalcomunicacao.organization.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.EquipeStatus;
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
 * Suíte de testes automatizados dos critérios de aceite FT-EQUIPE.
 */
@IntegrationTest
class EquipeAcceptanceIntegrationTest {

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
        equipeRepository.deleteAll();
        areaRepository.deleteAll();
        singularRepository.deleteAll();
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-001", type = AcceptanceCriterion.TestType.API)
    void atEquipe001_shouldCreateEquipeSuccessfully() throws Exception {
        AreaEntity area = seedActiveArea();
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/equipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "areaId": %d,
                                  "name": "Equipe Alpha"
                                }
                                """.formatted(area.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Equipe Alpha"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"))
                .andExpect(jsonPath("$.data.areaId").value(area.getId().intValue()));
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-001", type = AcceptanceCriterion.TestType.API)
    void atEquipe001_shouldRejectDuplicateName() throws Exception {
        AreaEntity area = seedActiveArea();
        seedEquipe(area.getId(), "Equipe Alpha");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/equipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "areaId": %d,
                                  "name": "Equipe Alpha"
                                }
                                """.formatted(area.getId())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-001", type = AcceptanceCriterion.TestType.API)
    void atEquipe001_shouldRejectUnauthorizedCreate() throws Exception {
        AreaEntity area = seedActiveArea();
        ColaboradorEntity regular = ensureRegularColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, regular.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/equipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "areaId": %d,
                                  "name": "Equipe Alpha"
                                }
                                """.formatted(area.getId())))
                .andExpect(status().isForbidden());
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-002", type = AcceptanceCriterion.TestType.API)
    void atEquipe002_shouldFindEquipeById() throws Exception {
        AreaEntity area = seedActiveArea();
        EquipeEntity equipe = seedEquipe(area.getId(), "Equipe Alpha");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/equipes/" + equipe.getId()).cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(equipe.getId().intValue()))
                .andExpect(jsonPath("$.data.name").value("Equipe Alpha"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-002", type = AcceptanceCriterion.TestType.API)
    void atEquipe002_shouldReturn404WhenNotFound() throws Exception {
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/equipes/999999").cookie(accessCookie))
                .andExpect(status().isNotFound());
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-002", type = AcceptanceCriterion.TestType.API)
    void atEquipe002_shouldReturn401WhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/equipes/1")).andExpect(status().isUnauthorized());
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-003", type = AcceptanceCriterion.TestType.API)
    void atEquipe003_shouldListEquipesWithFilters() throws Exception {
        AreaEntity area = seedActiveArea();
        seedEquipe(area.getId(), "Equipe Alpha");
        seedEquipe(area.getId(), "Equipe Beta");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/equipes")
                        .param("areaId", String.valueOf(area.getId()))
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
    @AcceptanceCriterion(value = "AT-EQUIPE-003", type = AcceptanceCriterion.TestType.API)
    void atEquipe003_shouldReturnEmptyList() throws Exception {
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/equipes").param("name", "INEXISTENTE").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isEmpty());
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-004", type = AcceptanceCriterion.TestType.API)
    void atEquipe004_shouldUpdateEquipe() throws Exception {
        AreaEntity area = seedActiveArea();
        EquipeEntity equipe = seedEquipe(area.getId(), "Equipe Alpha");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(put("/api/v1/equipes/" + equipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "name": "Equipe Alpha Renomeada"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Equipe Alpha Renomeada"))
                .andExpect(jsonPath("$.data.updatedAt").exists());
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-005", type = AcceptanceCriterion.TestType.API)
    void atEquipe005_shouldInactivateEquipe() throws Exception {
        AreaEntity area = seedActiveArea();
        EquipeEntity equipe = seedEquipe(area.getId(), "Equipe Alpha");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/equipes/" + equipe.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"INACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("INACTIVE"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-005", type = AcceptanceCriterion.TestType.API)
    void atEquipe005_shouldBlockInactivationWithActiveColaborador() throws Exception {
        AreaEntity area = seedActiveArea();
        EquipeEntity equipe = seedEquipe(area.getId(), "Equipe Alpha");
        ColaboradorEntity member = ensureRegularColaborador();
        member.setEquipeId(equipe.getId());
        colaboradorRepository.save(member);

        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/equipes/" + equipe.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"INACTIVE\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @AcceptanceCriterion(value = "AT-EQUIPE-005", type = AcceptanceCriterion.TestType.API)
    void atEquipe005_shouldReactivateEquipe() throws Exception {
        AreaEntity area = seedActiveArea();
        EquipeEntity equipe = seedEquipe(area.getId(), "Equipe Alpha");
        equipe.setAtivo(EquipeStatus.INACTIVE.toFlag());
        equipeRepository.save(equipe);

        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/equipes/" + equipe.getId() + "/status")
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

    private AreaEntity seedActiveArea() {
        SingularEntity singular = seedActiveSingular();
        AreaEntity area = new AreaEntity();
        area.setSingularId(singular.getId());
        area.setNome("Financeiro");
        area.setAtivo(AreaStatus.ACTIVE.toFlag());
        area.setDataCadastro(Instant.now());
        return areaRepository.save(area);
    }

    private EquipeEntity seedEquipe(Long areaId, String name) {
        EquipeEntity equipe = new EquipeEntity();
        equipe.setAreaId(areaId);
        equipe.setNome(name);
        equipe.setAtivo(EquipeStatus.ACTIVE.toFlag());
        equipe.setDataCadastro(Instant.now());
        return equipeRepository.save(equipe);
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
