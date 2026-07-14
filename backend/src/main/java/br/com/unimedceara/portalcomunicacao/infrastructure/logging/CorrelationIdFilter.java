package br.com.unimedceara.portalcomunicacao.infrastructure.logging;

import br.com.unimedceara.portalcomunicacao.shared.constants.HeaderConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Prepara o contexto de logging por requisição propagando o Correlation ID via MDC e headers HTTP.
 */
public class CorrelationIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            String correlationId = resolveCorrelationId(request);
            MdcUtils.put(LoggingConstants.MDC_CORRELATION_ID, correlationId);
            response.setHeader(HeaderConstants.X_CORRELATION_ID, correlationId);
            filterChain.doFilter(request, response);
        } finally {
            MdcUtils.clear();
        }
    }

    private String resolveCorrelationId(HttpServletRequest request) {
        String correlationId = request.getHeader(HeaderConstants.X_CORRELATION_ID);
        if (correlationId != null && !correlationId.isBlank()) {
            return correlationId.trim();
        }

        return CorrelationIdGenerator.generate();
    }
}
