package br.com.unimedceara.portalcomunicacao.infrastructure.logging;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Configura a infraestrutura compartilhada de logging da aplicação.
 */
@Configuration
public class LoggingConfiguration {

    /**
     * Registra o filtro de Correlation ID com prioridade elevada na cadeia de filtros.
     *
     * @return registro do filtro HTTP
     */
    @Bean
    public FilterRegistrationBean<CorrelationIdFilter> correlationIdFilterRegistration() {
        FilterRegistrationBean<CorrelationIdFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CorrelationIdFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
