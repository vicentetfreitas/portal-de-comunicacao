package br.com.unimedceara.portalcomunicacao.accesscontrol.application.service;

import br.com.unimedceara.portalcomunicacao.accesscontrol.acceptance.AcceptanceCriterion;
import br.com.unimedceara.portalcomunicacao.configuration.properties.SecurityProperties;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants.ACCESS_TOKEN_COOKIE;
import static br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants.REFRESH_TOKEN_COOKIE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthCookieServiceTest {

    @Mock
    private HttpServletResponse response;

    private AuthCookieService authCookieService;

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
        authCookieService = new AuthCookieService(properties);
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-012", type = AcceptanceCriterion.TestType.UNIT)
    void acAuth012_shouldConfigureHttpOnlyAndSecureOnAccessTokenCookie() {
        authCookieService.setAccessTokenCookie(response, "jwt-token-value");

        String setCookie = captureSetCookie();
        assertThat(setCookie).startsWith(ACCESS_TOKEN_COOKIE + "=");
        assertThat(setCookie).containsIgnoringCase("HttpOnly");
        assertThat(setCookie).containsIgnoringCase("Secure");
        assertThat(setCookie).contains("SameSite=Strict");
        assertThat(setCookie).contains("Path=/");
    }

    @Test
    @AcceptanceCriterion(value = "AC-AUTH-012", type = AcceptanceCriterion.TestType.UNIT)
    void acAuth012_shouldConfigureHttpOnlyAndSecureOnRefreshTokenCookie() {
        authCookieService.setRefreshTokenCookie(response, "refresh-token-value", false);

        String setCookie = captureSetCookie();
        assertThat(setCookie).startsWith(REFRESH_TOKEN_COOKIE + "=");
        assertThat(setCookie).containsIgnoringCase("HttpOnly");
        assertThat(setCookie).containsIgnoringCase("Secure");
        assertThat(setCookie).contains("Path=/api/v1/auth");
    }

    private String captureSetCookie() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(response).addHeader(org.mockito.ArgumentMatchers.eq("Set-Cookie"), captor.capture());
        return captor.getValue();
    }
}
