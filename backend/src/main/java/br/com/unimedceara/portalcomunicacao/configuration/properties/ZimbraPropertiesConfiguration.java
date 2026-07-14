package br.com.unimedceara.portalcomunicacao.configuration.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Habilita o binding das propriedades Zimbra.
 */
@Configuration
@EnableConfigurationProperties(ZimbraProperties.class)
public class ZimbraPropertiesConfiguration {
}
