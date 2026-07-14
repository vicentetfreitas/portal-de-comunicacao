package br.com.unimedceara.portalcomunicacao.configuration.properties;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Propriedades de configuração do módulo Integration (binding apenas — sem RestClient).
 */
@Validated
@ConfigurationProperties(prefix = "application.integration")
public record IntegrationProperties(
        @Min(1) int connectTimeoutMs,
        @Min(1) int readTimeoutMs,
        @Min(0) int maxRetryAttempts,
        @Min(1) int circuitBreakerThreshold) {
}
