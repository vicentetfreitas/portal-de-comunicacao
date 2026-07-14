package br.com.unimedceara.portalcomunicacao.configuration.properties;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Propriedades específicas do módulo Persistence que não possuem equivalente direto em {@code spring.datasource.*}.
 * Pool Hikari é aplicado via referência em {@code application.yaml} ({@code spring.datasource.hikari.*}).
 */
@Validated
@ConfigurationProperties(prefix = "application.persistence")
public record PersistenceProperties(
        @Min(1) int poolMaxSize,
        @Min(0) int poolMinIdle) {
}
