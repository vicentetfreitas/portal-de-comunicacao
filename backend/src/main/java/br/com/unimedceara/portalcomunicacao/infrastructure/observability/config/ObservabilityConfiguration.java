package br.com.unimedceara.portalcomunicacao.infrastructure.observability.config;

import br.com.unimedceara.portalcomunicacao.infrastructure.observability.filter.HttpRequestMetricsFilter;
import br.com.unimedceara.portalcomunicacao.infrastructure.observability.filter.RequestLoggingFilter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Registra filtros de observabilidade na cadeia HTTP.
 */
@Configuration
public class ObservabilityConfiguration {

    private static final int METRICS_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE + 10;
    private static final int REQUEST_LOGGING_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE + 20;

    /**
     * Filtro de métricas HTTP executado após o CorrelationIdFilter.
     *
     * @param meterRegistry registry Micrometer
     * @return registro do filtro
     */
    @Bean
    public FilterRegistrationBean<HttpRequestMetricsFilter> httpRequestMetricsFilterRegistration(
            MeterRegistry meterRegistry) {
        FilterRegistrationBean<HttpRequestMetricsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new HttpRequestMetricsFilter(meterRegistry));
        registration.setOrder(METRICS_FILTER_ORDER);
        return registration;
    }

    /**
     * Filtro de logging estruturado de requisições.
     *
     * @return registro do filtro
     */
    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilterRegistration() {
        FilterRegistrationBean<RequestLoggingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestLoggingFilter());
        registration.setOrder(REQUEST_LOGGING_FILTER_ORDER);
        return registration;
    }
}
