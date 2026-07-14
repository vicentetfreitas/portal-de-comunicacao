package br.com.unimedceara.portalcomunicacao.infrastructure.integration.interceptor;

import br.com.unimedceara.portalcomunicacao.infrastructure.logging.LoggingConstants;
import br.com.unimedceara.portalcomunicacao.infrastructure.logging.MdcUtils;
import br.com.unimedceara.portalcomunicacao.shared.constants.HeaderConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CorrelationIdInterceptorTest {

    private final CorrelationIdInterceptor interceptor = new CorrelationIdInterceptor();

    @AfterEach
    void tearDown() {
        MdcUtils.clear();
    }

    @Test
    void shouldPropagateCorrelationIdInOutboundRequest() throws IOException {
        String correlationId = UUID.randomUUID().toString();
        MdcUtils.put(LoggingConstants.MDC_CORRELATION_ID, correlationId);

        MockClientHttpRequest request = new MockClientHttpRequest(HttpMethod.GET, URI.create("http://localhost/test"));
        ClientHttpRequestExecution execution = (httpRequest, body) -> {
            assertThat(httpRequest.getHeaders().getFirst(HeaderConstants.X_CORRELATION_ID))
                    .isEqualTo(correlationId);
            return new MockClientHttpResponse(new byte[0], HttpStatus.OK);
        };

        interceptor.intercept(request, new byte[0], execution);
    }

    @Test
    void shouldNotAddCorrelationIdHeaderWhenMdcIsEmpty() throws IOException {
        MockClientHttpRequest request = new MockClientHttpRequest(HttpMethod.GET, URI.create("http://localhost/test"));
        ClientHttpRequestExecution execution = (httpRequest, body) -> {
            assertThat(httpRequest.getHeaders().getFirst(HeaderConstants.X_CORRELATION_ID)).isNull();
            return new MockClientHttpResponse(new byte[0], HttpStatus.OK);
        };

        interceptor.intercept(request, new byte[0], execution);
    }
}
