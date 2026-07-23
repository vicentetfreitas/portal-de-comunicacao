package br.com.unimedceara.portalcomunicacao.support.annotation;

import br.com.unimedceara.portalcomunicacao.support.database.IntegrationTestDatabaseCleanupConfiguration;
import br.com.unimedceara.portalcomunicacao.support.database.IntegrationTestDatabaseCleanupListener;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Habilita limpeza automática de dados via {@link IntegrationTestDatabaseCleanupListener}.
 * <p>
 * Desligada por padrão no perfil {@code test} (DEC-DB-023). Usar apenas quando
 * {@code application.persistence.integration-test-cleanup.enabled=true} e a política de dados permitir.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(IntegrationTestDatabaseCleanupConfiguration.class)
@TestExecutionListeners(
        value = IntegrationTestDatabaseCleanupListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public @interface EnableIntegrationTestDatabaseCleanup {
}
