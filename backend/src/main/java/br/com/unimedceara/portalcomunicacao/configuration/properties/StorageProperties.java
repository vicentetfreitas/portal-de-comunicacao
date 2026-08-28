package br.com.unimedceara.portalcomunicacao.configuration.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Propriedades de configuração do Object Storage S3-compatible (DEC-013, FT-DOCUMENTO).
 * Provedor concreto trocável (MinIO em desenvolvimento, gerenciado em produção) sem
 * mudar contrato de código — só estas propriedades.
 */
@Validated
@ConfigurationProperties(prefix = "application.storage")
public record StorageProperties(
        @NotBlank String endpoint,
        @NotBlank String region,
        @NotBlank String bucket,
        @NotBlank String accessKey,
        @NotBlank String secretKey,
        boolean pathStyleAccess) {
}
