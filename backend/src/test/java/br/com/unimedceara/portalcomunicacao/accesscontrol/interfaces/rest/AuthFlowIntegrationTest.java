package br.com.unimedceara.portalcomunicacao.accesscontrol.interfaces.rest;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.OAuthStateService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.RefreshTokenService;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.configuration.properties.AuthProperties;
import br.com.unimedceara.portalcomunicacao.support.fixture.builder.ColaboradorTestBuilder;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationRequest;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityValidationResult;
import br.com.unimedceara.portalcomunicacao.accesscontrol.application.port.IdentityCredentialValidator;
import br.com.unimedceara.portalcomunicacao.infrastructure.integration.client.IdentityProviderClient;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.base.AbstractTransactionalMockMvcIntegrationTest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Import(AuthFlowIntegrationTest.MockIdentityProviderConfiguration.class)
class AuthFlowIntegrationTest extends AbstractTransactionalMockMvcIntegrationTest {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private OAuthStateService oAuthStateService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private AuthProperties authProperties;

    @Test
    void shouldCompleteLoginCallbackMeRefreshAndLogoutFlow() throws Exception {
        ColaboradorTestBuilder.forFederation(authProperties.defaultFederationId())
                .email("colaborador@unimedceara.com.br")
                .nome("Colaborador Teste")
                .zimbraId("zimbra-id-test")
                .persist(colaboradorRepository);

        String state = oAuthStateService.createState(false);

        MvcResult callbackResult = mockMvc.perform(get("/api/v1/auth/callback")
                        .param("token", "valid-callback-token")
                        .param("state", state))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://localhost:4200/"))
                .andReturn();

        Cookie accessCookie = findCookie(callbackResult, SecurityConstants.ACCESS_TOKEN_COOKIE);
        Cookie refreshCookie = findCookie(callbackResult, SecurityConstants.REFRESH_TOKEN_COOKIE);
        assertThat(accessCookie).isNotNull();
        assertThat(refreshCookie).isNotNull();

        mockMvc.perform(get("/api/v1/auth/me").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("colaborador@unimedceara.com.br"))
                .andExpect(jsonPath("$.data.name").value("Colaborador Teste"))
                .andExpect(jsonPath("$.data.sessionId").isNotEmpty());

        MvcResult csrfResult = mockMvc.perform(get("/actuator/health")).andReturn();
        Cookie csrfCookie = findCookie(csrfResult, SecurityConstants.CSRF_COOKIE);
        assertThat(csrfCookie).isNotNull();

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .cookie(refreshCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Access token renovado"));

        mockMvc.perform(post("/api/v1/auth/logout")
                        .cookie(accessCookie, refreshCookie, csrfCookie)
                        .header(SecurityConstants.CSRF_HEADER, csrfCookie.getValue()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldRedirectLoginToZimbra() throws Exception {
        mockMvc.perform(get("/api/v1/auth/login"))
                .andExpect(status().isFound())
                .andExpect(result -> {
                    String location = result.getResponse().getHeader("Location");
                    assertThat(location).isNotNull();
                    assertThat(URI.create(location).getHost()).isEqualTo("localhost");
                });
    }

    private Cookie findCookie(MvcResult result, String name) {
        if (result.getResponse().getCookies() == null) {
            return null;
        }
        for (Cookie cookie : result.getResponse().getCookies()) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    @TestConfiguration
    static class MockIdentityProviderConfiguration {

        @Bean
        @Primary
        IdentityCredentialValidator mockIdentityCredentialValidator() {
            return new IdentityCredentialValidator() {
                @Override
                public IdentityValidationResult validateCredentials(String email, String password) {
                    throw new IllegalStateException("Not used in callback flow test");
                }

                @Override
                public IdentityValidationResult validateOpaqueToken(String opaqueToken) {
                    if ("valid-callback-token".equals(opaqueToken)) {
                        return new IdentityValidationResult(
                                "colaborador@unimedceara.com.br",
                                "Colaborador Teste",
                                "zimbra-id-test");
                    }
                    throw new IllegalStateException("Invalid token");
                }
            };
        }

        @Bean
        @Primary
        IdentityProviderClient mockIdentityProviderClient(IdentityCredentialValidator validator) {
            return new IdentityProviderClient() {
                @Override
                public IdentityValidationResult validateIdentity(IdentityValidationRequest request) {
                    return validator.validateOpaqueToken(request.validationToken());
                }

                @Override
                public URI buildAuthorizationUrl(String state, String callbackUrl) {
                    return URI.create("http://localhost:9000/auth?state=" + state);
                }
            };
        }
    }
}
