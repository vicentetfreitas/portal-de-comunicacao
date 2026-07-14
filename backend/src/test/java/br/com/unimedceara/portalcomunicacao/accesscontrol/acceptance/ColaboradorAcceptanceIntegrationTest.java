package br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.AreaStatus;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.EquipeStatus;
import br.com.unimedceara.portalcomunicacao.organization.domain.model.SingularStatus;
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
    private OrganizationalSeed seed;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        colaboradorRepository.deleteAll();
        equipeRepository.deleteAll();
        areaRepository.deleteAll();
        singularRepository.deleteAll();
        seed = seedOrganization();
        ensureAdmin();
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
                                  "email": "joao@unimedceara.com.br"
                                }
                                """
                                .formatted(
                                        authProperties.defaultFederationId(),
                                        seed.singularId(),
                                        seed.areaId(),
                                        seed.equipeId())))
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
                        .param("teamId", String.valueOf(seed.equipeId()))
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
                                  "email": "update@unimedceara.com.br"
                                }
                                """
                                .formatted(seed.singularId(), seed.areaId(), seed.equipeId())))
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
                                  "email": "invalido@unimedceara.com.br"
                                }
                                """
                                .formatted(authProperties.defaultFederationId(), seed.areaId())))
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
                                  "email": "dup@unimedceara.com.br"
                                }
                                """
                                .formatted(authProperties.defaultFederationId(), seed.equipeId())))
                .andExpect(status().isUnprocessableEntity());
    }

    private ColaboradorEntity seedColaborador(String email) {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setFederacaoId(authProperties.defaultFederationId());
        colaborador.setSingularId(seed.singularId());
        colaborador.setAreaId(seed.areaId());
        colaborador.setEquipeId(seed.equipeId());
        colaborador.setNome("Colaborador");
        colaborador.setEmail(email);
        colaborador.setAtivo(EquipeStatus.ACTIVE.toFlag());
        colaborador.setDataCadastro(Instant.now());
        return colaboradorRepository.save(colaborador);
    }

    private OrganizationalSeed seedOrganization() {
        SingularEntity singular = new SingularEntity();
        singular.setFederacaoId(authProperties.defaultFederationId());
        singular.setNome("Singular");
        singular.setSigla("SG");
        singular.setCodigoUnimed("010");
        singular.setAtivo(SingularStatus.ACTIVE.toFlag());
        singular.setDataCadastro(Instant.now());
        singular = singularRepository.save(singular);

        AreaEntity area = new AreaEntity();
        area.setSingularId(singular.getId());
        area.setNome("Area");
        area.setAtivo(AreaStatus.ACTIVE.toFlag());
        area.setDataCadastro(Instant.now());
        area = areaRepository.save(area);

        EquipeEntity equipe = new EquipeEntity();
        equipe.setAreaId(area.getId());
        equipe.setNome("Equipe");
        equipe.setAtivo(EquipeStatus.ACTIVE.toFlag());
        equipe.setDataCadastro(Instant.now());
        equipe = equipeRepository.save(equipe);

        return new OrganizationalSeed(singular.getId(), area.getId(), equipe.getId());
    }

    private void ensureAdmin() {
        if (colaboradorRepository.findByEmailIgnoreCase("colaborador@unimedceara.com.br").isEmpty()) {
            ColaboradorEntity admin = new ColaboradorEntity();
            admin.setEmail("colaborador@unimedceara.com.br");
            admin.setNome("Admin");
            admin.setZimbraId("zimbra-admin");
            admin.setAtivo(EquipeStatus.ACTIVE.toFlag());
            admin.setFederacaoId(authProperties.defaultFederationId());
            admin.setDataCadastro(Instant.now());
            colaboradorRepository.save(admin);
        }
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

    private record OrganizationalSeed(Long singularId, Long areaId, Long equipeId) {
    }
}
