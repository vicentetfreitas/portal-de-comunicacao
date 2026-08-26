package br.com.unimedceara.portalcomunicacao.configuration.persistence;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.type.SqlTypes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Escopo explícito de entidades e repositórios de produção (bounded contexts persistidos).
 * <p>
 * Evita que artefatos de teste no classpath (ex.: {@code TestAuditableEntity}) participem do
 * {@code SessionFactory} em {@code @SpringBootTest}, o que quebrava {@code ddl-auto=validate} no Oracle.
 */
@Configuration
@ConditionalOnProperty(prefix = "application.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
@EntityScan(
        basePackages = {
            "br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.entity",
            "br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.entity",
            "br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.entity"
        })
@EnableJpaRepositories(
        basePackages = {
            "br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository",
            "br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository",
            "br.com.unimedceara.portalcomunicacao.documento.infrastructure.persistence.repository"
        })
public class JpaPersistenceConfiguration {

    /**
     * Oracle {@code TIMESTAMP(6)} / {@code DATE} + {@link java.time.Instant}: evita ORA-18716 ao ler
     * colunas sem fuso (DEC-DB-020 / Hibernate 7).
     */
    @Bean
    static HibernatePropertiesCustomizer oracleInstantJdbcMappingCustomizer() {
        return hibernateProperties -> {
            hibernateProperties.put(AvailableSettings.JDBC_TIME_ZONE, "UTC");
            hibernateProperties.put(AvailableSettings.PREFERRED_INSTANT_JDBC_TYPE, SqlTypes.TIMESTAMP);
        };
    }
}
