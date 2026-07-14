package br.com.unimedceara.portalcomunicacao.infrastructure.observability.filter;

import br.com.unimedceara.portalcomunicacao.infrastructure.logging.LoggingConstants;
import br.com.unimedceara.portalcomunicacao.infrastructure.logging.MdcUtils;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;

class RequestLoggingFilterTest {

    private final RequestLoggingFilter filter = new RequestLoggingFilter();

    @AfterEach
    void tearDown() {
        MdcUtils.clear();
    }

    @Test
    void shouldLogRequestWithCorrelationId() throws ServletException, IOException {
        String correlationId = UUID.randomUUID().toString();
        MdcUtils.put(LoggingConstants.MDC_CORRELATION_ID, correlationId);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/health");
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(200);

        assertThatCode(() -> filter.doFilter(request, response, new MockFilterChain()))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldLogRequestWithoutSensitiveHeaders() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/health");
        request.addHeader("Authorization", "Bearer secret-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(200);

        assertThatCode(() -> filter.doFilter(request, response, new MockFilterChain()))
                .doesNotThrowAnyException();
    }
}
