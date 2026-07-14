package br.com.unimedceara.portalcomunicacao.configuration.locale;

import br.com.unimedceara.portalcomunicacao.configuration.properties.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

/**
 * Configura a localidade padrão da aplicação.
 */
@Configuration
public class LocaleConfiguration {

    /**
     * Define e expõe a localidade padrão com base nas propriedades da aplicação.
     *
     * @param applicationProperties propriedades compartilhadas da aplicação
     * @return localidade configurada
     */
    @Bean
    public Locale applicationLocale(ApplicationProperties applicationProperties) {
        Locale locale = Locale.forLanguageTag(applicationProperties.locale());
        Locale.setDefault(locale);
        return locale;
    }
}
