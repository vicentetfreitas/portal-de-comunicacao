package br.com.unimedceara.portalcomunicacao.organization.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.AreaEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.FederacaoEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity.SingularEntity;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.FederacaoRepository;
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
 * Suíte de testes automatizados dos critérios de aceite FT-SINGULAR.
 */
@IntegrationTest
class SingularAcceptanceIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private FederacaoRepository federacaoRepository;

    @Autowired
    private SingularRepository singularRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private AuthProperties authProperties;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        areaRepository.deleteAll();
        singularRepository.deleteAll();
        federacaoRepository.deleteAll();
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-001", type = AcceptanceCriterion.TestType.API)
    void atSingular001_shouldCreateSingularSuccessfully() throws Exception {
        FederacaoEntity federacao = seedActiveFederacao();
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/singulares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federacaoId": %d,
                                  "name": "Unimed Ceará",
                                  "acronym": "UNI-CE",
                                  "codigoUnimed": "UC001"
                                }
                                """.formatted(federacao.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Unimed Ceará"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"))
                .andExpect(jsonPath("$.data.federacaoId").value(federacao.getId().intValue()));
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-001", type = AcceptanceCriterion.TestType.API)
    void atSingular001_shouldRejectDuplicateAcronym() throws Exception {
        FederacaoEntity federacao = seedActiveFederacao();
        seedSingular(federacao.getId(), "Outra Singular", "UNI-CE", "UC999");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/singulares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federacaoId": %d,
                                  "name": "Unimed Ceará",
                                  "acronym": "UNI-CE",
                                  "codigoUnimed": "UC001"
                                }
                                """.formatted(federacao.getId())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-001", type = AcceptanceCriterion.TestType.API)
    void atSingular001_shouldRejectDuplicateCodigoUnimed() throws Exception {
        FederacaoEntity federacao = seedActiveFederacao();
        seedSingular(federacao.getId(), "Outra Singular", "OUT", "UC001");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/singulares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federacaoId": %d,
                                  "name": "Unimed Ceará",
                                  "acronym": "UNI-CE",
                                  "codigoUnimed": "UC001"
                                }
                                """.formatted(federacao.getId())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-001", type = AcceptanceCriterion.TestType.API)
    void atSingular001_shouldRejectInvalidFederacao() throws Exception {
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/singulares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federacaoId": 999999,
                                  "name": "Unimed Ceará",
                                  "acronym": "UNI-CE",
                                  "codigoUnimed": "UC001"
                                }
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-001", type = AcceptanceCriterion.TestType.API)
    void atSingular001_shouldRejectUnauthorizedCreate() throws Exception {
        FederacaoEntity federacao = seedActiveFederacao();
        ColaboradorEntity regular = ensureRegularColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, regular.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(post("/api/v1/singulares")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "federacaoId": %d,
                                  "name": "Unimed Ceará",
                                  "acronym": "UNI-CE",
                                  "codigoUnimed": "UC001"
                                }
                                """.formatted(federacao.getId())))
                .andExpect(status().isForbidden());
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-002", type = AcceptanceCriterion.TestType.API)
    void atSingular002_shouldFindSingularById() throws Exception {
        FederacaoEntity federacao = seedActiveFederacao();
        SingularEntity singular = seedSingular(federacao.getId(), "Unimed Ceará", "UNI-CE", "UC001");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/singulares/" + singular.getId()).cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(singular.getId().intValue()))
                .andExpect(jsonPath("$.data.name").value("Unimed Ceará"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-002", type = AcceptanceCriterion.TestType.API)
    void atSingular002_shouldReturn404WhenNotFound() throws Exception {
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/singulares/999999").cookie(accessCookie))
                .andExpect(status().isNotFound());
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-002", type = AcceptanceCriterion.TestType.API)
    void atSingular002_shouldRejectUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/singulares/1")).andExpect(status().isUnauthorized());
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-003", type = AcceptanceCriterion.TestType.API)
    void atSingular003_shouldListWithStatusFilter() throws Exception {
        FederacaoEntity federacao = seedActiveFederacao();
        seedSingular(federacao.getId(), "Singular Ativa", "ATV", "UC100");
        SingularEntity inactive = seedSingular(federacao.getId(), "Singular Inativa", "INA", "UC101");
        inactive.setAtivo(SingularStatus.INACTIVE.toFlag());
        singularRepository.save(inactive);

        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/singulares")
                        .param("status", "ACTIVE")
                        .param("page", "0")
                        .param("size", "10")
                        .cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[?(@.status == 'ACTIVE')]").exists())
                .andExpect(jsonPath("$.data.content[?(@.status == 'INACTIVE')]").doesNotExist());
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-003", type = AcceptanceCriterion.TestType.API)
    void atSingular003_shouldReturnEmptyPage() throws Exception {
        FederacaoEntity federacao = seedActiveFederacao();
        seedSingular(federacao.getId(), "Singular Ativa", "ATV", "UC100");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());

        mockMvc.perform(get("/api/v1/singulares").param("name", "INEXISTENTE").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isEmpty())
                .andExpect(jsonPath("$.data.totalElements").value(0));
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-004", type = AcceptanceCriterion.TestType.API)
    void atSingular004_shouldUpdateSingular() throws Exception {
        FederacaoEntity federacao = seedActiveFederacao();
        SingularEntity singular = seedSingular(federacao.getId(), "Unimed Ceará", "UNI-CE", "UC001");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(put("/api/v1/singulares/" + singular.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "name": "Unimed Ceará Atualizada",
                                  "acronym": "UNI-CE",
                                  "codigoUnimed": "UC001"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Unimed Ceará Atualizada"))
                .andExpect(jsonPath("$.data.federacaoId").value(federacao.getId().intValue()))
                .andExpect(jsonPath("$.data.updatedAt").exists());
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-004", type = AcceptanceCriterion.TestType.API)
    void atSingular004_shouldRejectDuplicateAcronymOnUpdate() throws Exception {
        FederacaoEntity federacao = seedActiveFederacao();
        SingularEntity singular = seedSingular(federacao.getId(), "Singular A", "SIG-A", "UCA");
        seedSingular(federacao.getId(), "Singular B", "SIG-B", "UCB");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(put("/api/v1/singulares/" + singular.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "name": "Singular A",
                                  "acronym": "SIG-B",
                                  "codigoUnimed": "UCA"
                                }
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-004", type = AcceptanceCriterion.TestType.API)
    void atSingular004_shouldRejectUpdateWhenFederacaoInactive() throws Exception {
        FederacaoEntity federacao = seedActiveFederacao();
        SingularEntity singular = seedSingular(federacao.getId(), "Unimed Ceará", "UNI-CE", "UC001");
        federacao.setAtivo(SingularStatus.INACTIVE.toFlag());
        federacaoRepository.save(federacao);

        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(put("/api/v1/singulares/" + singular.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "name": "Unimed Ceará Atualizada",
                                  "acronym": "UNI-CE",
                                  "codigoUnimed": "UC001"
                                }
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-004", type = AcceptanceCriterion.TestType.API)
    void atSingular004_shouldReturn404WhenNotFound() throws Exception {
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(put("/api/v1/singulares/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "name": "Unimed Ceará",
                                  "acronym": "UNI-CE",
                                  "codigoUnimed": "UC001"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-004", type = AcceptanceCriterion.TestType.API)
    void atSingular004_shouldRejectUnauthorizedUpdate() throws Exception {
        FederacaoEntity federacao = seedActiveFederacao();
        SingularEntity singular = seedSingular(federacao.getId(), "Unimed Ceará", "UNI-CE", "UC001");
        ColaboradorEntity regular = ensureRegularColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, regular.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(put("/api/v1/singulares/" + singular.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("""
                                {
                                  "name": "Unimed Ceará",
                                  "acronym": "UNI-CE",
                                  "codigoUnimed": "UC001"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-005", type = AcceptanceCriterion.TestType.API)
    void atSingular005_shouldInactivateSingular() throws Exception {
        FederacaoEntity federacao = seedActiveFederacao();
        SingularEntity singular = seedSingular(federacao.getId(), "Unimed Ceará", "UNI-CE", "UC001");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/singulares/" + singular.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"INACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("INACTIVE"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-005", type = AcceptanceCriterion.TestType.API)
    void atSingular005_shouldBlockInactivationWithActiveArea() throws Exception {
        FederacaoEntity federacao = seedActiveFederacao();
        SingularEntity singular = seedSingular(federacao.getId(), "Unimed Ceará", "UNI-CE", "UC001");
        seedArea(singular.getId(), "Financeiro");
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/singulares/" + singular.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"INACTIVE\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-005", type = AcceptanceCriterion.TestType.API)
    void atSingular005_shouldReactivateSingular() throws Exception {
        FederacaoEntity federacao = seedActiveFederacao();
        SingularEntity singular = seedSingular(federacao.getId(), "Unimed Ceará", "UNI-CE", "UC001");
        singular.setAtivo(SingularStatus.INACTIVE.toFlag());
        singularRepository.save(singular);

        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/singulares/" + singular.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"ACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    @AcceptanceCriterion(value = "AT-SINGULAR-005", type = AcceptanceCriterion.TestType.API)
    void atSingular005_shouldReturn404WhenNotFound() throws Exception {
        ColaboradorEntity admin = ensureAdminColaborador();
        Cookie accessCookie = TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
        Cookie csrfCookie = obtainCsrfCookie();

        mockMvc.perform(patch("/api/v1/singulares/999999/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(accessCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"INACTIVE\"}"))
                .andExpect(status().isNotFound());
    }

    private FederacaoEntity seedActiveFederacao() {
        FederacaoEntity federacao = new FederacaoEntity();
        federacao.setNome("Federação Teste");
        federacao.setSigla("FED");
        federacao.setCodigoUnimed("FED001");
        federacao.setNumeroRegistroAns("123456");
        federacao.setAtivo(SingularStatus.ACTIVE.toFlag());
        federacao.setDataCadastro(Instant.now());
        return federacaoRepository.save(federacao);
    }

    private SingularEntity seedSingular(Long federacaoId, String name, String acronym, String codigoUnimed) {
        SingularEntity singular = new SingularEntity();
        singular.setFederacaoId(federacaoId);
        singular.setNome(name);
        singular.setSigla(acronym);
        singular.setCodigoUnimed(codigoUnimed);
        singular.setAtivo(SingularStatus.ACTIVE.toFlag());
        singular.setDataCadastro(Instant.now());
        return singularRepository.save(singular);
    }

    private AreaEntity seedArea(Long singularId, String name) {
        AreaEntity area = new AreaEntity();
        area.setSingularId(singularId);
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
