package br.com.unimedceara.portalcomunicacao.configuration.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Habilita o binding das propriedades do módulo Auth (FT-AUTH).
 */
@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthPropertiesConfiguration {
}
