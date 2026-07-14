package br.com.unimedceara.portalcomunicacao.configuration.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Propriedades compartilhadas da aplicação carregadas com o prefixo {@code application}.
 *
 * @param name     nome da aplicação
 * @param version  versão da aplicação
 * @param timezone fuso horário padrão
 * @param locale   localidade padrão
 */
@Validated
@ConfigurationProperties(prefix = "application")
public record ApplicationProperties(
        @NotBlank String name,
        @NotBlank String version,
        @NotBlank String timezone,
        @NotBlank String locale) {
}
