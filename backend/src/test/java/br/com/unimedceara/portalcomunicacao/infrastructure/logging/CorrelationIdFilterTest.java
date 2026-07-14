package br.com.unimedceara.portalcomunicacao.infrastructure.logging;

import br.com.unimedceara.portalcomunicacao.shared.constants.HeaderConstants;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CorrelationIdFilterTest {

    private CorrelationIdFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    @BeforeEach
    void setUp() {
        filter = new CorrelationIdFilter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
    }

    @AfterEach
    void tearDown() {
        MdcUtils.clear();
    }

    @Test
    void shouldUseExistingCorrelationIdFromRequestHeader() throws ServletException, IOException {
        String correlationId = UUID.randomUUID().toString();
        request.addHeader(HeaderConstants.X_CORRELATION_ID, correlationId);

        filter.doFilter(request, response, filterChain);

        assertThat(response.getHeader(HeaderConstants.X_CORRELATION_ID)).isEqualTo(correlationId);
    }

    @Test
    void shouldGenerateCorrelationIdWhenHeaderIsAbsent() throws ServletException, IOException {
        filter.doFilter(request, response, filterChain);

        String responseCorrelationId = response.getHeader(HeaderConstants.X_CORRELATION_ID);
        assertThat(responseCorrelationId).isNotBlank();
        assertThat(UUID.fromString(responseCorrelationId)).isNotNull();
    }

    @Test
    void shouldPopulateMdcDuringRequestProcessing() throws ServletException, IOException {
        String correlationId = UUID.randomUUID().toString();
        request.addHeader(HeaderConstants.X_CORRELATION_ID, correlationId);
        filterChain = new MockFilterChain() {
            @Override
            public void doFilter(jakarta.servlet.ServletRequest servletRequest, jakarta.servlet.ServletResponse servletResponse)
                    throws IOException, ServletException {
                assertThat(MdcUtils.get(LoggingConstants.MDC_CORRELATION_ID)).isEqualTo(correlationId);
                super.doFilter(servletRequest, servletResponse);
            }
        };

        filter.doFilter(request, response, filterChain);
    }

    @Test
    void shouldClearMdcAfterRequestProcessing() throws ServletException, IOException {
        filter.doFilter(request, response, filterChain);

        assertThat(MdcUtils.get(LoggingConstants.MDC_CORRELATION_ID)).isNull();
    }
}
