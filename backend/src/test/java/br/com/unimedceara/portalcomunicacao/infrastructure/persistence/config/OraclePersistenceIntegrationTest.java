package br.com.unimedceara.portalcomunicacao.infrastructure.persistence.config;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
@EnabledIfEnvironmentVariable(named = "SPRING_DATASOURCE_URL", matches = ".+")
@Transactional(readOnly = true)
class OraclePersistenceIntegrationTest {

    private static final String APPLICATION_USER = "UNMPORTCOM_APP";

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Test
    void shouldConnectAsApplicationUser() throws Exception {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT USER FROM DUAL");
                ResultSet rs = ps.executeQuery()) {
            assertThat(rs.next()).isTrue();
            assertThat(rs.getString(1)).isEqualToIgnoringCase(APPLICATION_USER);
        }
    }

    @Test
    void shouldExecuteReadOnlyTransactionAgainstOracle() {
        Number result = (Number) entityManager
                .createNativeQuery("SELECT 1 FROM DUAL")
                .getSingleResult();

        assertThat(result.intValue()).isEqualTo(1);
    }
}
