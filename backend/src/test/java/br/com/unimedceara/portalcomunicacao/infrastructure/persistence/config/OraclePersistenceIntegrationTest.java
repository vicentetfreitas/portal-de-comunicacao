package br.com.unimedceara.portalcomunicacao.infrastructure.persistence.config;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
@EnabledIfEnvironmentVariable(named = "DB_URL", matches = ".+")
@Transactional(readOnly = true)
class OraclePersistenceIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldExecuteReadOnlyTransactionAgainstOracle() {
        Number result = (Number) entityManager
                .createNativeQuery("SELECT 1 FROM DUAL")
                .getSingleResult();

        assertThat(result.intValue()).isEqualTo(1);
    }
}
