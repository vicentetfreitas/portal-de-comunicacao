package br.com.unimedceara.portalcomunicacao.organization.acceptance;

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
        colaboradorRepository.deleteAll();
        equipeRepository.deleteAll();
        areaRepository.deleteAll();
        singularRepository.deleteAll();
    }

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
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setFederacaoId(authProperties.defaultFederationId());
        colaborador.setEquipeId(equipe.getId());
        colaborador.setAreaId(area.getId());
        colaborador.setNome("Membro");
        colaborador.setEmail("membro@unimedceara.com.br");
        colaborador.setAtivo(EquipeStatus.ACTIVE.toFlag());
        colaborador.setDataCadastro(Instant.now());
        colaboradorRepository.save(colaborador);

        Cookie csrfCookie = obtainCsrfCookie();
        mockMvc.perform(patch("/api/v1/equipes/" + equipe.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(adminCookie(), csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue())
                        .content("{\"status\": \"INACTIVE\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    private AreaEntity seedArea() {
        SingularEntity singular = new SingularEntity();
        singular.setFederacaoId(authProperties.defaultFederationId());
        singular.setNome("Singular");
        singular.setSigla("SG");
        singular.setCodigoUnimed("001");
        singular.setAtivo(SingularStatus.ACTIVE.toFlag());
        singular.setDataCadastro(Instant.now());
        singular = singularRepository.save(singular);

        AreaEntity area = new AreaEntity();
        area.setSingularId(singular.getId());
        area.setNome("Area");
        area.setAtivo(AreaStatus.ACTIVE.toFlag());
        area.setDataCadastro(Instant.now());
        return areaRepository.save(area);
    }

    private EquipeEntity seedEquipe(AreaEntity area) {
        EquipeEntity equipe = new EquipeEntity();
        equipe.setAreaId(area.getId());
        equipe.setNome("Equipe");
        equipe.setAtivo(EquipeStatus.ACTIVE.toFlag());
        equipe.setDataCadastro(Instant.now());
        return equipeRepository.save(equipe);
    }

    private Cookie adminCookie() {
        ColaboradorEntity admin = colaboradorRepository
                .findByEmailIgnoreCase("colaborador@unimedceara.com.br")
                .orElseGet(() -> {
                    ColaboradorEntity c = new ColaboradorEntity();
                    c.setEmail("colaborador@unimedceara.com.br");
                    c.setNome("Admin");
                    c.setZimbraId("zimbra-admin");
                    c.setAtivo(EquipeStatus.ACTIVE.toFlag());
                    c.setFederacaoId(authProperties.defaultFederationId());
                    c.setDataCadastro(Instant.now());
                    return colaboradorRepository.save(c);
                });
        return TestSecurityContextFactory.jwtCookie(jwtTokenService, admin.getId());
    }

    private Cookie obtainCsrfCookie() throws Exception {
        return mockMvc.perform(get("/actuator/health"))
                .andReturn()
                .getResponse()
                .getCookie(SecurityConstants.CSRF_COOKIE);
    }
}
