package br.com.unimedceara.portalcomunicacao.infrastructure.observability.filter;

import br.com.unimedceara.portalcomunicacao.infrastructure.logging.LoggingConstants;
import br.com.unimedceara.portalcomunicacao.infrastructure.logging.MdcUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Emite log estruturado por requisição HTTP sem dados sensíveis.
 */
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        long startMillis = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            logRequest(request, response, startMillis);
        }
    }

    private void logRequest(HttpServletRequest request, HttpServletResponse response, long startMillis) {
        long durationMs = System.currentTimeMillis() - startMillis;
        String correlationId = MdcUtils.get(LoggingConstants.MDC_CORRELATION_ID);

        log.info(
                "request completed method={} uri={} status={} durationMs={} correlationId={}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                durationMs,
                correlationId);
    }
}
