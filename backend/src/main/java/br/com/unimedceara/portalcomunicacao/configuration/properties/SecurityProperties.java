package br.com.unimedceara.portalcomunicacao.configuration.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Propriedades de configuração do módulo Security (binding apenas — sem runtime de segurança).
 */
@Validated
@ConfigurationProperties(prefix = "application.security")
public record SecurityProperties(
        @NotBlank String jwtIssuer,
        @NotBlank String jwtSecret,
        @Min(1) int jwtAccessTtlMinutes,
        @Min(1) int refreshTokenTtlHours,
        @Min(1) int refreshTokenRememberMeDays,
        @Min(1) int maxConcurrentSessions,
        boolean csrfEnabled,
        @NotEmpty List<String> corsAllowedOrigins) {
}
