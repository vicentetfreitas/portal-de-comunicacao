package br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.OAuthStateService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.ColaboradorEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.PapelAtribuicaoEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity.PapelEntity;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.PapelAtribuicaoRepository;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.PapelRepository;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityProviderClient;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationResult;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.base.AbstractTransactionalMockMvcIntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.data.IntegrationTestUniqueData;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.ColaboradorTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.PapelAtribuicaoTestBuilder;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.PapelTestBuilder;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Suíte de aceite da evolução de FT-SESSION: contexto operacional baseado em
 * {@code PAPEL_ATRIBUICAO}.
 * <p>
 * Rastreabilidade: {@code specs/features/session/specification.md} (RN-SESSION-006 a 010).
 */
@IntegrationTest
@Import(TestIdentityProviderConfiguration.class)
class SessionAtribuicaoAcceptanceIntegrationTest extends AbstractTransactionalMockMvcIntegrationTest {

    @Autowired
    private OAuthStateService oAuthStateService;

    @Autowired
    private IdentityProviderClient identityProviderClient;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private PapelRepository papelRepository;

    @Autowired
    private PapelAtribuicaoRepository papelAtribuicaoRepository;

    @Autowired
    private AuthProperties authProperties;

    @Autowired
    private tools.jackson.databind.json.JsonMapper jsonMapper;

    private TestIdentityProviderClient testIdentityProviderClient() {
        return (TestIdentityProviderClient) identityProviderClient;
    }

    @BeforeEach
    void resetIdentityProvider() {
        testIdentityProviderClient().reset();
    }

    @Test
    void shouldAutoSelectWhenExactlyOneEligibleAssignment() throws Exception {
        ColaboradorEntity colaborador = ensureColaborador("uma-atrib@unimedceara.com.br", "zimbra-um");
        PapelEntity papel = PapelTestBuilder.named("COLABORADOR_TESTE_" + IntegrationTestUniqueData.uniqueId())
                .persist(papelRepository);
        PapelAtribuicaoEntity atribuicao = PapelAtribuicaoTestBuilder.of(colaborador, papel)
                .persist(papelAtribuicaoRepository);

        Cookie accessCookie = login(colaborador.getEmail());

        mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.eligibleAssignments.length()").value(1))
                .andExpect(jsonPath("$.data.activeAssignment.id").value(atribuicao.getId()))
                .andExpect(jsonPath("$.data.activeAssignment.papel").value(papel.getNome()));
    }

    @Test
    void shouldAutoSelectOnMeWhenAssignmentBecomesEligibleAfterLogin() throws Exception {
        ColaboradorEntity colaborador = ensureColaborador("atrib-pos-login@unimedceara.com.br", "zimbra-pos-login");

        Cookie accessCookie = login(colaborador.getEmail());
        mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.eligibleAssignments.length()").value(0))
                .andExpect(jsonPath("$.data.activeAssignment").doesNotExist());

        PapelEntity papel = PapelTestBuilder.named("PAPEL_POS_LOGIN_" + IntegrationTestUniqueData.uniqueId())
                .persist(papelRepository);
        PapelAtribuicaoEntity atribuicao = PapelAtribuicaoTestBuilder.of(colaborador, papel)
                .persist(papelAtribuicaoRepository);

        mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.eligibleAssignments.length()").value(1))
                .andExpect(jsonPath("$.data.activeAssignment.id").value(atribuicao.getId()));
    }

    @Test
    void shouldNotAutoSelectWhenMultipleEligibleAssignments() throws Exception {
        ColaboradorEntity colaborador = ensureColaborador("multi-atrib@unimedceara.com.br", "zimbra-multi");
        PapelEntity papelA = PapelTestBuilder.named("PAPEL_A_" + IntegrationTestUniqueData.uniqueId())
                .persist(papelRepository);
        PapelEntity papelB = PapelTestBuilder.named("PAPEL_B_" + IntegrationTestUniqueData.uniqueId())
                .persist(papelRepository);
        PapelAtribuicaoTestBuilder.of(colaborador, papelA).persist(papelAtribuicaoRepository);
        PapelAtribuicaoTestBuilder.of(colaborador, papelB).persist(papelAtribuicaoRepository);

        Cookie accessCookie = login(colaborador.getEmail());

        mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.eligibleAssignments.length()").value(2))
                .andExpect(jsonPath("$.data.activeAssignment").doesNotExist());
    }

    @Test
    void shouldActivateValidOwnAssignment() throws Exception {
        ColaboradorEntity colaborador = ensureColaborador("selecao-valida@unimedceara.com.br", "zimbra-sel");
        PapelEntity papelA = PapelTestBuilder.named("PAPEL_A_" + IntegrationTestUniqueData.uniqueId())
                .persist(papelRepository);
        PapelEntity papelB = PapelTestBuilder.named("PAPEL_B_" + IntegrationTestUniqueData.uniqueId())
                .persist(papelRepository);
        PapelAtribuicaoTestBuilder.of(colaborador, papelA).persist(papelAtribuicaoRepository);
        PapelAtribuicaoEntity atribuicaoB = PapelAtribuicaoTestBuilder.of(colaborador, papelB)
                .persist(papelAtribuicaoRepository);

        Cookie accessCookie = login(colaborador.getEmail());

        MvcResult result = mockMvc.perform(post("/api/v1/auth/atribuicoes/" + atribuicaoB.getId() + "/ativar")
                        .with(csrf())
                        .cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.activeAssignment.id").value(atribuicaoB.getId()))
                .andExpect(jsonPath("$.data.activeAssignment.papel").value(papelB.getNome()))
                .andReturn();

        Cookie newAccessCookie = findCookie(result, SecurityConstants.ACCESS_TOKEN_COOKIE);
        assertThat(newAccessCookie).isNotNull();

        mockMvc.perform(get("/api/v1/auth/me").cookie(newAccessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.activeAssignment.id").value(atribuicaoB.getId()));
    }

    @Test
    void shouldRejectSelectingAnotherUsersAssignment() throws Exception {
        ColaboradorEntity owner = ensureColaborador("dono-atrib@unimedceara.com.br", "zimbra-dono");
        ColaboradorEntity requester = ensureColaborador("solicitante@unimedceara.com.br", "zimbra-solic");
        PapelEntity papel = PapelTestBuilder.named("PAPEL_ALHEIO_" + IntegrationTestUniqueData.uniqueId())
                .persist(papelRepository);
        PapelAtribuicaoEntity ownersAssignment = PapelAtribuicaoTestBuilder.of(owner, papel)
                .persist(papelAtribuicaoRepository);

        Cookie requesterAccessCookie = login(requester.getEmail());

        mockMvc.perform(post("/api/v1/auth/atribuicoes/" + ownersAssignment.getId() + "/ativar")
                        .with(csrf())
                        .cookie(requesterAccessCookie))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/v1/auth/me").cookie(requesterAccessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.activeAssignment").doesNotExist());
    }

    @Test
    void shouldRejectSelectingInactiveAssignment() throws Exception {
        ColaboradorEntity colaborador = ensureColaborador("atrib-inativa@unimedceara.com.br", "zimbra-inativa");
        PapelEntity papel = PapelTestBuilder.named("PAPEL_INATIVO_" + IntegrationTestUniqueData.uniqueId())
                .persist(papelRepository);
        PapelAtribuicaoEntity inactiveAssignment = PapelAtribuicaoTestBuilder.of(colaborador, papel)
                .ativo("N")
                .persist(papelAtribuicaoRepository);

        Cookie accessCookie = login(colaborador.getEmail());

        mockMvc.perform(post("/api/v1/auth/atribuicoes/" + inactiveAssignment.getId() + "/ativar")
                        .with(csrf())
                        .cookie(accessCookie))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectSelectingAssignmentOutsideVigencia() throws Exception {
        ColaboradorEntity colaborador = ensureColaborador("atrib-expirada@unimedceara.com.br", "zimbra-expirada");
        PapelEntity papel = PapelTestBuilder.named("PAPEL_EXPIRADO_" + IntegrationTestUniqueData.uniqueId())
                .persist(papelRepository);
        PapelAtribuicaoEntity expiredAssignment = PapelAtribuicaoTestBuilder.of(colaborador, papel)
                .expirada()
                .persist(papelAtribuicaoRepository);
        PapelAtribuicaoEntity futureAssignment = PapelAtribuicaoTestBuilder.of(colaborador, papel)
                .futura()
                .persist(papelAtribuicaoRepository);

        Cookie accessCookie = login(colaborador.getEmail());

        mockMvc.perform(post("/api/v1/auth/atribuicoes/" + expiredAssignment.getId() + "/ativar")
                        .with(csrf())
                        .cookie(accessCookie))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/v1/auth/atribuicoes/" + futureAssignment.getId() + "/ativar")
                        .with(csrf())
                        .cookie(accessCookie))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldSwitchAssignmentWithoutNewLogin() throws Exception {
        ColaboradorEntity colaborador = ensureColaborador("troca-contexto@unimedceara.com.br", "zimbra-troca");
        PapelEntity papelA = PapelTestBuilder.named("PAPEL_A_" + IntegrationTestUniqueData.uniqueId())
                .persist(papelRepository);
        PapelEntity papelB = PapelTestBuilder.named("PAPEL_B_" + IntegrationTestUniqueData.uniqueId())
                .persist(papelRepository);
        PapelAtribuicaoEntity atribuicaoA = PapelAtribuicaoTestBuilder.of(colaborador, papelA)
                .persist(papelAtribuicaoRepository);
        PapelAtribuicaoEntity atribuicaoB = PapelAtribuicaoTestBuilder.of(colaborador, papelB)
                .persist(papelAtribuicaoRepository);

        MvcResult loginResult = performSuccessfulCallback(colaborador.getEmail());
        Cookie initialAccessCookie = findCookie(loginResult, SecurityConstants.ACCESS_TOKEN_COOKIE);
        Cookie refreshCookie = findCookie(loginResult, SecurityConstants.REFRESH_TOKEN_COOKIE);
        String sessionIdBefore = readSessionId(initialAccessCookie);

        MvcResult firstSelection = mockMvc.perform(
                        post("/api/v1/auth/atribuicoes/" + atribuicaoA.getId() + "/ativar")
                                .with(csrf())
                                .cookie(initialAccessCookie))
                .andExpect(status().isOk())
                .andReturn();
        Cookie accessAfterA = findCookie(firstSelection, SecurityConstants.ACCESS_TOKEN_COOKIE);
        assertThat(findCookie(firstSelection, SecurityConstants.REFRESH_TOKEN_COOKIE))
                .as("seleção de atribuição não deve emitir novo Refresh Token (sem novo login)")
                .isNull();

        MvcResult secondSelection = mockMvc.perform(
                        post("/api/v1/auth/atribuicoes/" + atribuicaoB.getId() + "/ativar")
                                .with(csrf())
                                .cookie(accessAfterA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.activeAssignment.id").value(atribuicaoB.getId()))
                .andReturn();
        Cookie accessAfterB = findCookie(secondSelection, SecurityConstants.ACCESS_TOKEN_COOKIE);

        assertThat(readSessionId(accessAfterB)).isEqualTo(sessionIdBefore);
        assertThat(refreshCookie).isNotNull();

        mockMvc.perform(get("/api/v1/auth/me").cookie(accessAfterB))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.sessionId").value(sessionIdBefore))
                .andExpect(jsonPath("$.data.activeAssignment.id").value(atribuicaoB.getId()));
    }

    @Test
    void shouldPreserveActiveAssignmentAcrossRefresh() throws Exception {
        ColaboradorEntity colaborador = ensureColaborador("refresh-contexto@unimedceara.com.br", "zimbra-refresh");
        PapelEntity papelA = PapelTestBuilder.named("PAPEL_A_" + IntegrationTestUniqueData.uniqueId())
                .persist(papelRepository);
        PapelEntity papelB = PapelTestBuilder.named("PAPEL_B_" + IntegrationTestUniqueData.uniqueId())
                .persist(papelRepository);
        PapelAtribuicaoTestBuilder.of(colaborador, papelA).persist(papelAtribuicaoRepository);
        PapelAtribuicaoEntity atribuicaoB = PapelAtribuicaoTestBuilder.of(colaborador, papelB)
                .persist(papelAtribuicaoRepository);

        MvcResult loginResult = performSuccessfulCallback(colaborador.getEmail());
        Cookie initialAccessCookie = findCookie(loginResult, SecurityConstants.ACCESS_TOKEN_COOKIE);
        Cookie refreshCookie = findCookie(loginResult, SecurityConstants.REFRESH_TOKEN_COOKIE);

        MvcResult selection = mockMvc.perform(
                        post("/api/v1/auth/atribuicoes/" + atribuicaoB.getId() + "/ativar")
                                .with(csrf())
                                .cookie(initialAccessCookie))
                .andExpect(status().isOk())
                .andReturn();
        Cookie accessAfterSelection = findCookie(selection, SecurityConstants.ACCESS_TOKEN_COOKIE);

        MvcResult refreshResult = mockMvc.perform(post("/api/v1/auth/refresh")
                        .with(csrf())
                        .cookie(refreshCookie)
                        .cookie(accessAfterSelection))
                .andExpect(status().isOk())
                .andReturn();
        Cookie accessAfterRefresh = findCookie(refreshResult, SecurityConstants.ACCESS_TOKEN_COOKIE);
        assertThat(accessAfterRefresh).isNotNull();

        mockMvc.perform(get("/api/v1/auth/me").cookie(accessAfterRefresh))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.activeAssignment.id").value(atribuicaoB.getId()));
    }

    @Test
    void shouldDropActiveAssignmentOnRefreshWhenNoLongerEligible() throws Exception {
        ColaboradorEntity colaborador = ensureColaborador("refresh-invalida@unimedceara.com.br", "zimbra-refresh-inv");
        PapelEntity papel = PapelTestBuilder.named("PAPEL_TEMP_" + IntegrationTestUniqueData.uniqueId())
                .persist(papelRepository);
        PapelAtribuicaoEntity atribuicao = PapelAtribuicaoTestBuilder.of(colaborador, papel)
                .persist(papelAtribuicaoRepository);

        MvcResult loginResult = performSuccessfulCallback(colaborador.getEmail());
        Cookie accessCookie = findCookie(loginResult, SecurityConstants.ACCESS_TOKEN_COOKIE);
        Cookie refreshCookie = findCookie(loginResult, SecurityConstants.REFRESH_TOKEN_COOKIE);
        mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie))
                .andExpect(jsonPath("$.data.activeAssignment.id").value(atribuicao.getId()));

        atribuicao.setAtivo("N");
        papelAtribuicaoRepository.save(atribuicao);

        MvcResult refreshResult = mockMvc.perform(post("/api/v1/auth/refresh")
                        .with(csrf())
                        .cookie(refreshCookie)
                        .cookie(accessCookie))
                .andExpect(status().isOk())
                .andReturn();
        Cookie accessAfterRefresh = findCookie(refreshResult, SecurityConstants.ACCESS_TOKEN_COOKIE);

        mockMvc.perform(get("/api/v1/auth/me").cookie(accessAfterRefresh))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.activeAssignment").doesNotExist());
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private ColaboradorEntity ensureColaborador(String email, String zimbraId) {
        return ColaboradorTestBuilder.forFederation(authProperties.defaultFederationId())
                .email(email)
                .nome("Colaborador " + email)
                .zimbraId(zimbraId)
                .persist(colaboradorRepository);
    }

    private Cookie login(String email) throws Exception {
        return findCookie(performSuccessfulCallback(email), SecurityConstants.ACCESS_TOKEN_COOKIE);
    }

    private MvcResult performSuccessfulCallback(String email) throws Exception {
        testIdentityProviderClient().setValidationBehavior(token ->
                new IdentityValidationResult(email, "Colaborador " + email, "zimbra-" + email));
        String state = oAuthStateService.createState(false);
        return mockMvc.perform(get("/api/v1/auth/callback")
                        .param("token", TestIdentityProviderClient.VALID_TOKEN)
                        .param("state", state)
                        .header("User-Agent", "session-atribuicao-test-agent"))
                .andExpect(status().isFound())
                .andReturn();
    }

    private String readSessionId(Cookie accessCookie) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie))
                .andExpect(status().isOk())
                .andReturn();
        return jsonMapper.readTree(result.getResponse().getContentAsString())
                .at("/data/sessionId")
                .asText();
    }

    private Cookie findCookie(MvcResult result, String name) {
        if (result.getResponse().getCookies() != null) {
            for (Cookie cookie : result.getResponse().getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        Collection<String> setCookies = result.getResponse().getHeaders("Set-Cookie");
        return setCookies.stream()
                .filter(header -> header.startsWith(name + "="))
                .map(header -> new Cookie(name, header.substring(name.length() + 1).split(";", 2)[0]))
                .findFirst()
                .orElse(null);
    }
}
