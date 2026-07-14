package br.com.unimedceara.portalcomunicacao.configuration.properties;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Habilita o binding das propriedades do módulo Persistence.
 */
@Configuration
@EnableConfigurationProperties(PersistenceProperties.class)
public class PersistencePropertiesConfiguration {

    /**
     * Suporte a {@code application.persistence.ddl-auto} em cenários de teste que sobrescrevem o ddl-auto
     * do perfil ativo. Propriedade legada — preferir {@code spring.jpa.hibernate.ddl-auto}.
     */
    @Bean
    @ConditionalOnProperty(prefix = "application.persistence", name = "ddl-auto")
    static HibernatePropertiesCustomizer persistenceDdlAutoCustomizer(Environment environment) {
        return hibernateProperties -> hibernateProperties.put(
                AvailableSettings.HBM2DDL_AUTO,
                environment.getRequiredProperty("application.persistence.ddl-auto"));
    }
}
