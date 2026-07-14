package br.com.unimedceara.portalcomunicacao.infrastructure.observability.filter;

import br.com.unimedceara.portalcomunicacao.infrastructure.observability.ObservabilityConstants;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Registra métricas HTTP (counter e timer) para requisições inbound.
 */
public class HttpRequestMetricsFilter extends OncePerRequestFilter {

    private final MeterRegistry meterRegistry;

    /**
     * Constrói o filtro de métricas HTTP.
     *
     * @param meterRegistry registry Micrometer
     */
    public HttpRequestMetricsFilter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        long startNanos = System.nanoTime();
        try {
            filterChain.doFilter(request, response);
        } finally {
            recordMetrics(request, response, startNanos);
        }
    }

    private void recordMetrics(HttpServletRequest request, HttpServletResponse response, long startNanos) {
        String method = request.getMethod();
        String status = String.valueOf(response.getStatus());

        meterRegistry.counter(
                ObservabilityConstants.HTTP_REQUESTS_TOTAL,
                "method", method,
                "status", status)
                .increment();

        Timer.builder(ObservabilityConstants.HTTP_REQUESTS_DURATION)
                .description("HTTP request duration")
                .tag("method", method)
                .tag("status", status)
                .register(meterRegistry)
                .record(System.nanoTime() - startNanos, TimeUnit.NANOSECONDS);
    }
}
