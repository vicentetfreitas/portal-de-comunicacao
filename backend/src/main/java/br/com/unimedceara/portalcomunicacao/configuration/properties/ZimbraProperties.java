package br.com.unimedceara.portalcomunicacao.configuration.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Propriedades de configuração Zimbra (estrutura apenas — sem cliente HTTP).
 */
@Validated
@ConfigurationProperties(prefix = "application.zimbra")
public record ZimbraProperties(
        @NotBlank String authUrl,
        @NotBlank String validateUrl,
        @Min(1) int timeoutMs) {
}
