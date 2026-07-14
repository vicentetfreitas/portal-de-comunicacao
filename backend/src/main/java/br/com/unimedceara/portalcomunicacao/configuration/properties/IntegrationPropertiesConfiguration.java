package br.com.unimedceara.portalcomunicacao.configuration.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Habilita o binding das propriedades do módulo Integration.
 */
@Configuration
@EnableConfigurationProperties(IntegrationProperties.class)
public class IntegrationPropertiesConfiguration {
}
