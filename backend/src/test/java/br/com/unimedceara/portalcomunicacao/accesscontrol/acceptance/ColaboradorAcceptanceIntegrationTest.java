package br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.domain.model.ColaboradorStatus;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
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
 * Suíte de testes automatizados dos critérios de aceite FT-COLABORADOR.
 */
@IntegrationTest
class ColaboradorAcceptanceIntegrationTest {

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
        colaboradorRepository.deleteAll();
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-001", type = AcceptanceCriterion.TestType.API)
    void atColaborador001_shouldCreateColaboradorSuccessfully() throws Exception {
        OrganizationalSeed seed = seedOrganizationalContext();
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/colaboradores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federationId": %d,
                                  "singularId": %d,
                                  "areaId": %d,
                                  "name": "Maria Silva",
                                  "email": "maria.silva@unimedceara.com.br"
                                }
                                """.formatted(
                                authProperties.defaultFederationId(),
                                seed.singular().getId(),
                                seed.area().getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Maria Silva"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"))
                .andExpect(jsonPath("$.data.areaId").value(seed.area().getId().intValue()));
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-001", type = AcceptanceCriterion.TestType.API)
    void atColaborador001_shouldRejectDuplicateEmail() throws Exception {
        OrganizationalSeed seed = seedOrganizationalContext();
        ColaboradorEntity admin = ensureAdminColaborador();
        seedColaborador(seed, "maria.silva@unimedceara.com.br", "Maria Silva");
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/colaboradores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federationId": %d,
                                  "singularId": %d,
                                  "areaId": %d,
                                  "name": "Maria Duplicada",
                                  "email": "maria.silva@unimedceara.com.br"
                                }
                                """.formatted(
                                authProperties.defaultFederationId(),
                                seed.singular().getId(),
                                seed.area().getId())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-001", type = AcceptanceCriterion.TestType.API)
    void atColaborador001_shouldRejectUnauthorizedCreate() throws Exception {
        OrganizationalSeed seed = seedOrganizationalContext();
        ColaboradorEntity regular = ensureRegularColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, regular.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/colaboradores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federationId": %d,
                                  "singularId": %d,
                                  "areaId": %d,
                                  "name": "Maria Silva",
                                  "email": "maria@unimedceara.com.br"
                                }
                                """.formatted(
                                authProperties.defaultFederationId(),
                                seed.singular().getId(),
                                seed.area().getId())))
                .andExpect(status().isForbidden());
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-002", type = AcceptanceCriterion.TestType.API)
    void atColaborador002_shouldFindColaboradorById() throws Exception {
        OrganizationalSeed seed = seedOrganizationalContext();
        ColaboradorEntity colaborador = seedColaborador(seed, "maria@unimedceara.com.br", "Maria");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/colaboradores/" + colaborador.getId()).cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(colaborador.getId().intValue()))
                .andExpect(jsonPath("$.data.name").value("Maria"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-002", type = AcceptanceCriterion.TestType.API)
    void atColaborador002_shouldReturn404WhenNotFound() throws Exception {
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/colaboradores/999999").cookie(accessCookie))
                .andExpect(status().isNotFound());
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-002", type = AcceptanceCriterion.TestType.API)
    void atColaborador002_shouldReturn401WhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/colaboradores/1")).andExpect(status().isUnauthorized());
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-003", type = AcceptanceCriterion.TestType.API)
    void atColaborador003_shouldListColaboradoresWithFilters() throws Exception {
        OrganizationalSeed seed = seedOrganizationalContext();
        seedColaborador(seed, "maria@unimedceara.com.br", "Maria");
        seedColaborador(seed, "joao@unimedceara.com.br", "Joao");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/colaboradores")
                        .param("areaId", String.valueOf(seed.area().getId()))
                        .param("status", "ACTIVE")
                        .param("page", "0")
                        .param("size", "10")
                        .cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-004", type = AcceptanceCriterion.TestType.API)
    void atColaborador004_shouldUpdateColaborador() throws Exception {
        OrganizationalSeed seed = seedOrganizationalContext();
        ColaboradorEntity colaborador = seedColaborador(seed, "maria@unimedceara.com.br", "Maria");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(put("/api/v1/colaboradores/" + colaborador.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "singularId": %d,
                                  "areaId": %d,
                                  "name": "Maria Silva Atualizada",
                                  "jobTitle": "Analista"
                                }
                                """.formatted(seed.singular().getId(), seed.area().getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Maria Silva Atualizada"))
                .andExpect(jsonPath("$.data.jobTitle").value("Analista"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-005", type = AcceptanceCriterion.TestType.API)
    void atColaborador005_shouldInactivateColaborador() throws Exception {
        OrganizationalSeed seed = seedOrganizationalContext();
        ColaboradorEntity colaborador = seedColaborador(seed, "maria@unimedceara.com.br", "Maria");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/colaboradores/" + colaborador.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"INACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("INACTIVE"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-COLABORADOR-005", type = AcceptanceCriterion.TestType.API)
    void atColaborador005_shouldBlockInactivationWithActiveSubordinate() throws Exception {
        OrganizationalSeed seed = seedOrganizationalContext();
        ColaboradorEntity manager = seedColaborador(seed, "gestor@unimedceara.com.br", "Gestor");
        ColaboradorEntity subordinate = seedColaborador(seed, "sub@unimedceara.com.br", "Subordinado");
        subordinate.setGestorId(manager.getId());
        colaboradorRepository.save(subordinate);

        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/colaboradores/" + manager.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"INACTIVE\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    private record OrganizationalSeed(SingularEntity singular, AreaEntity area) {
    }

    private OrganizationalSeed seedOrganizationalContext() {
        SingularEntity singular = new SingularEntity();
        singular.setFederacaoId(authProperties.defaultFederationId());
        singular.setNome("Singular Teste");
        singular.setSigla("ST");
        singular.setCodigoUnimed("001");
        singular.setAtivo(AreaStatus.ACTIVE.toFlag());
        singular.setDataCadastro(Instant.now());
        singular = singularRepository.save(singular);

        AreaEntity area = new AreaEntity();
        area.setSingularId(singular.getId());
        area.setNome("Financeiro");
        area.setAtivo(AreaStatus.ACTIVE.toFlag());
        area.setDataCadastro(Instant.now());
        area = areaRepository.save(area);

        return new OrganizationalSeed(singular, area);
    }

    private ColaboradorEntity seedColaborador(OrganizationalSeed seed, String email, String name) {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setFederacaoId(authProperties.defaultFederationId());
        colaborador.setSingularId(seed.singular().getId());
        colaborador.setAreaId(seed.area().getId());
        colaborador.setNome(name);
        colaborador.setEmail(email);
        colaborador.setAtivo(ColaboradorStatus.ACTIVE.toFlag());
        colaborador.setDataCadastro(Instant.now());
        return colaboradorRepository.save(colaborador);
    }

    private ColaboradorEntity ensureAdminColaborador() {
        return colaboradorRepository
                .findByEmailIgnoreCase("colaborador@unimedceara.com.br")
                .orElseGet(() -> {
                    ColaboradorEntity colaborador = new ColaboradorEntity();
                    colaborador.setEmail("colaborador@unimedceara.com.br");
                    colaborador.setNome("Admin Teste");
                    colaborador.setZimbraId("zimbra-admin");
                    colaborador.setAtivo(ColaboradorStatus.ACTIVE.toFlag());
                    colaborador.setFederacaoId(authProperties.defaultFederationId());
                    colaborador.setDataCadastro(Instant.now());
                    return colaboradorRepository.save(colaborador);
                });
    }

    private ColaboradorEntity ensureRegularColaborador() {
        return colaboradorRepository
                .findByEmailIgnoreCase("regular@unimedceara.com.br")
                .orElseGet(() -> {
                    ColaboradorEntity colaborador = new ColaboradorEntity();
                    colaborador.setEmail("regular@unimedceara.com.br");
                    colaborador.setNome("Regular Teste");
                    colaborador.setZimbraId("zimbra-regular");
                    colaborador.setAtivo(ColaboradorStatus.ACTIVE.toFlag());
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
