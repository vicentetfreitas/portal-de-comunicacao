package br.com.unimedceara.portalcomunicacao.support.web;

import br.com.unimedceara.portalcomunicacao.shared.constants.SecurityConstants;
import jakarta.servlet.http.Cookie;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public final class MockMvcTestSupport {

    private MockMvcTestSupport() {}

    public static Cookie obtainCsrfCookie(MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get("/actuator/health"))
                .andReturn()
                .getResponse()
                .getCookie(SecurityConstants.CSRF_COOKIE);
    }
}
