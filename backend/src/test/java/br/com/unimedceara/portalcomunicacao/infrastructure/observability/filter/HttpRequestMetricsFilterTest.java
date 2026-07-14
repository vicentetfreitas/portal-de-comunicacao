package br.com.unimedceara.portalcomunicacao.infrastructure.observability.filter;

import br.com.unimedceara.portalcomunicacao.infrastructure.observability.ObservabilityConstants;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestMetricsFilterTest {

    private MeterRegistry meterRegistry;
    private HttpRequestMetricsFilter filter;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        filter = new HttpRequestMetricsFilter(meterRegistry);
    }

    @Test
    void shouldRegisterHttpRequestMetrics() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/health");
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(200);

        filter.doFilter(request, response, new MockFilterChain());

        assertThat(meterRegistry.find(ObservabilityConstants.HTTP_REQUESTS_TOTAL)
                .tag("method", "GET")
                .tag("status", "200")
                .counter()
                .count()).isEqualTo(1.0);
        assertThat(meterRegistry.find(ObservabilityConstants.HTTP_REQUESTS_DURATION)
                .tag("method", "GET")
                .tag("status", "200")
                .timer()
                .count()).isEqualTo(1);
    }
}
