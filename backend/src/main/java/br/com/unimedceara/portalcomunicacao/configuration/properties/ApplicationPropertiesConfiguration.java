package br.com.unimedceara.portalcomunicacao.configuration.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Habilita o binding das propriedades compartilhadas da aplicação.
 */
@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationPropertiesConfiguration {
}
