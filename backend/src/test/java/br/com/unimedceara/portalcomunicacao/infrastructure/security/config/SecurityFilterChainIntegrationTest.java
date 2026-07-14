package br.com.unimedceara.portalcomunicacao.infrastructure.security.config;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@TestPropertySource(locations = "classpath:pf-sec-test.properties")
@Import(SecurityFilterChainIntegrationTest.SecurityTestConfiguration.class)
class SecurityFilterChainIntegrationTest {

    private static final String PROTECTED_PATH = "/api/v1/test/protected";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JwtTokenService jwtTokenService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    void shouldAllowPublicHealthEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnUnauthorizedForProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get(PROTECTED_PATH))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.path").value(PROTECTED_PATH));
    }

    @Test
    void shouldIssueCsrfCookieOnRequest() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(cookie().exists(SecurityConstants.CSRF_COOKIE));
    }

    @Test
    void shouldRejectMutableRequestWithoutCsrfToken() throws Exception {
        mockMvc.perform(post(PROTECTED_PATH))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowCorsPreflightForConfiguredOrigin() throws Exception {
        mockMvc.perform(options(PROTECTED_PATH)
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }

    @Test
    void shouldAllowProtectedEndpointWithSignedJwtCookie() throws Exception {
        String token = jwtTokenService.issueToken(1L, "session-test", "user@test.com", "User Test");
        Cookie accessToken = new Cookie(SecurityConstants.ACCESS_TOKEN_COOKIE, token);

        mockMvc.perform(get(PROTECTED_PATH).cookie(accessToken))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class SecurityTestConfiguration {

        @Bean
        SecurityTestController securityTestController() {
            return new SecurityTestController();
        }
    }

    @RestController
    static class SecurityTestController {

        @GetMapping(PROTECTED_PATH)
        String getProtected() {
            return "protected";
        }

        @PostMapping(PROTECTED_PATH)
        String postProtected() {
            return "protected";
        }
    }
}
