package br.com.unimedceara.portalcomunicacao.infrastructure.persistence.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Garante que a suíte Oracle (DEC-DB-023) conecta como application user, não como schema owner (DEC-DB-024).
 */
@SpringBootTest(
        classes = DataSourceAutoConfiguration.class,
        properties = {
            "spring.autoconfigure.exclude="
                    + "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration,"
                    + "org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration"
        })
@Import(DataSourceAutoConfiguration.class)
@ActiveProfiles("test")
class ApplicationUserConnectionIntegrationTest {

    private static final String APPLICATION_USER = "UNMPORTCOM_APP";
    private static final String SCHEMA_OWNER = "UNMPORTCOM";

    @Autowired
    private DataSource dataSource;

    @Test
    void shouldConnectAsApplicationUserNotSchemaOwner() throws Exception {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT USER FROM DUAL");
                ResultSet rs = ps.executeQuery()) {
            assertThat(rs.next()).isTrue();
            String sessionUser = rs.getString(1);
            assertThat(sessionUser)
                    .as("spring.datasource.username deve ser UNMPORTCOM_APP (INFRA-DB-01)")
                    .isEqualToIgnoringCase(APPLICATION_USER);
            assertThat(sessionUser)
                    .as("backend não deve conectar como schema owner")
                    .isNotEqualToIgnoringCase(SCHEMA_OWNER);
        }
    }

    @Test
    void shouldSeeOwnerTablesForHibernateValidation() throws Exception {
        String sql =
                """
                SELECT COUNT(*)
                  FROM all_tables
                 WHERE owner = ?
                   AND table_name = 'FEDERACAO'
                """;
        try (Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, SCHEMA_OWNER);
            try (ResultSet rs = ps.executeQuery()) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getInt(1))
                        .as("UNMPORTCOM_APP deve enxergar UNMPORTCOM.FEDERACAO (grants + CURRENT_SCHEMA)")
                        .isGreaterThanOrEqualTo(1);
            }
        }
    }
}
