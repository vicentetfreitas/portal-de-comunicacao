package br.com.unimedceara.portalcomunicacao.infrastructure.observability.config;

import br.com.unimedceara.portalcomunicacao.infrastructure.observability.ObservabilityConstants;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.micrometer.metrics.autoconfigure.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de métricas Micrometer com convenção portal.* (CD-S1A-005).
 */
@Configuration
public class MetricsConfiguration {

    /**
     * Aplica tags comuns às métricas da aplicação.
     *
     * @return customizer do registry Micrometer
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> observabilityMetricsCustomizer() {
        return registry -> registry.config()
                .commonTags("application", ObservabilityConstants.METRIC_PREFIX);
    }
}
