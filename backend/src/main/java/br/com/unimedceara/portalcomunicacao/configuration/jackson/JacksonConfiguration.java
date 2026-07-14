package br.com.unimedceara.portalcomunicacao.configuration.jackson;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.cfg.DateTimeFeature;

/**
 * Configuração compartilhada do {@link tools.jackson.databind.json.JsonMapper}.
 */
@Configuration
public class JacksonConfiguration {

    /**
     * Customiza o builder do {@link tools.jackson.databind.json.JsonMapper} da aplicação.
     *
     * @return customizador do builder Jackson
     */
    @Bean
    public JsonMapperBuilderCustomizer jacksonObjectMapperBuilderCustomizer() {
        return builder -> builder
                .findAndAddModules()
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
