package br.com.unimedceara.portalcomunicacao.configuration.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Propriedades de configuração do fluxo de autenticação FT-AUTH.
 */
@Validated
@ConfigurationProperties(prefix = "application.auth")
public record AuthProperties(
        @NotBlank String frontendRedirectUrl,
        @Min(1) long defaultFederationId,
        List<String> sessionAdministratorEmails) {

    public AuthProperties {
        if (sessionAdministratorEmails == null) {
            sessionAdministratorEmails = List.of();
        }
    }
}
