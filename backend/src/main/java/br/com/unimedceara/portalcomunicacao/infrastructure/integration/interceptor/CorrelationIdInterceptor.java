package br.com.unimedceara.portalcomunicacao.infrastructure.integration.interceptor;

import br.com.unimedceara.portalcomunicacao.infrastructure.logging.LoggingConstants;
import br.com.unimedceara.portalcomunicacao.infrastructure.logging.MdcUtils;
import br.com.unimedceara.portalcomunicacao.shared.constants.HeaderConstants;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Propaga o Correlation ID do MDC para requisições HTTP outbound.
 */
@Component
public class CorrelationIdInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        String correlationId = MdcUtils.get(LoggingConstants.MDC_CORRELATION_ID);
        if (correlationId != null && !correlationId.isBlank()) {
            request.getHeaders().set(HeaderConstants.X_CORRELATION_ID, correlationId);
        }
        return execution.execute(request, body);
    }
}
