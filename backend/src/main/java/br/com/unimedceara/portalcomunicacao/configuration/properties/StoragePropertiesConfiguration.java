package br.com.unimedceara.portalcomunicacao.configuration.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Habilita o binding das propriedades do Object Storage (DEC-013, FT-DOCUMENTO).
 */
@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StoragePropertiesConfiguration {
}
