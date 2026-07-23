package br.com.unimedceara.portalcomunicacao.support.config;

import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * INFRA-DB-02: alinha metadados Hibernate com {@code UNMPORTCOM_APP} + owner {@code UNMPORTCOM} (DEC-DB-024).
 */
@Configuration
@Profile("test")
public class OracleIntegrationTestPersistenceConfiguration {

    @Bean
    static HibernatePropertiesCustomizer oracleApplicationUserMetadataCustomizer() {
        return hibernateProperties -> hibernateProperties.put(
                "hibernate.hbm2ddl.jdbc_metadata_extraction_strategy", "individually");
    }
}
