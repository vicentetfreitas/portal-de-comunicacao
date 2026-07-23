package br.com.unimedceara.portalcomunicacao.support.database;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Invoca {@link IntegrationTestDatabaseCleaner} antes de cada método de teste em classes {@code @IntegrationTest}.
 */
public class IntegrationTestDatabaseCleanupListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        var context = testContext.getApplicationContext();
        if (!context.getBeansOfType(IntegrationTestDatabaseCleaner.class).isEmpty()) {
            context.getBean(IntegrationTestDatabaseCleaner.class).clean();
        }
    }
}
