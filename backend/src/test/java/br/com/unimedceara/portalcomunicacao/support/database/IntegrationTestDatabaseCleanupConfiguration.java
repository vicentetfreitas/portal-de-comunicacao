package br.com.unimedceara.portalcomunicacao.support.database;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.AuthSessaoRepository;
import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.AreaRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.EquipeRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.FederacaoRepository;
import br.com.unimedceara.portalcomunicacao.organization.infrastructure.persistence.repository.SingularRepository;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Registra {@link IntegrationTestDatabaseCleaner} quando a limpeza automática estiver habilitada
 * ({@code application.persistence.integration-test-cleanup.enabled=true}, DEC-DB-023).
 */
@TestConfiguration
@ConditionalOnProperty(
        prefix = "application.persistence.integration-test-cleanup",
        name = "enabled",
        havingValue = "true")
public class IntegrationTestDatabaseCleanupConfiguration {

    @Bean
    IntegrationTestDatabaseCleaner integrationTestDatabaseCleaner(
            AuthSessaoRepository authSessaoRepository,
            ColaboradorRepository colaboradorRepository,
            EquipeRepository equipeRepository,
            AreaRepository areaRepository,
            SingularRepository singularRepository,
            FederacaoRepository federacaoRepository,
            EntityManager entityManager) {
        return new IntegrationTestDatabaseCleaner(
                authSessaoRepository,
                colaboradorRepository,
                equipeRepository,
                areaRepository,
                singularRepository,
                federacaoRepository,
                entityManager);
    }
}
