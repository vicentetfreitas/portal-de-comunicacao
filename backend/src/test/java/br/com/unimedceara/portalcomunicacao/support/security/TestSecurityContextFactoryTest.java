package br.com.unimedceara.portalcomunicacao.support.security;

import br.com.unimedceara.portalcomunicacao.accesscontrol.application.service.JwtTokenService;
import br.com.unimedceara.portalcomunicacao.configuration.properties.SecurityProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TestSecurityContextFactoryTest {

    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        SecurityProperties properties = new SecurityProperties(
                "portal-comunicacao",
                "test-jwt-secret-32-characters-minimum",
                15,
                8,
                30,
                3,
                true,
                List.of("http://localhost:4200"));
        jwtTokenService = new JwtTokenService(properties, JsonMapper.builder().build());
    }

    @AfterEach
    void tearDown() {
        TestSecurityContextFactory.clear();
    }

    @Test
    void shouldSetAuthenticatedUserInSecurityContext() {
        TestSecurityContextFactory.setAuthenticatedUser("user-1");

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("user-1");
    }

    @Test
    void shouldClearSecurityContext() {
        TestSecurityContextFactory.setAuthenticatedUser("user-1");

        TestSecurityContextFactory.clear();

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldBuildSignedJwtToken() {
        String token = TestSecurityContextFactory.buildJwtToken(jwtTokenService, 1L);

        assertThat(token.split("\\.")).hasSize(3);
        assertThat(jwtTokenService.validateAndParse(token)).isPresent();
    }

    @Test
    void shouldBuildJwtCookieWithAccessTokenName() {
        assertThat(TestSecurityContextFactory.jwtCookie(jwtTokenService, 1L).getName())
                .isEqualTo("access_token");
        assertThat(TestSecurityContextFactory.jwtCookie(jwtTokenService, 1L).getValue()).isNotBlank();
    }
}