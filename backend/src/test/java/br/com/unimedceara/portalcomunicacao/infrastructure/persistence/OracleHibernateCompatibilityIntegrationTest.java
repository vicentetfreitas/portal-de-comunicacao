package br.com.unimedceara.portalcomunicacao.infrastructure.persistence;

import br.com.unimedceara.portalcomunicacao.accesscontrol.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.unimedceara.portalcomunicacao.support.annotation.IntegrationTest;
import br.com.unimedceara.portalcomunicacao.support.data.IntegrationTestUniqueData;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.community.dialect.OracleLegacyDialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * INFRA-BE-08 / INFRA-BE-09: dialect Oracle × paginação (ROWNUM vs FETCH FIRST) e método
 * {@code existsByEmailIgnoreCase}. O dialect em runtime é obtido via {@link SessionFactoryImplementor}
 * (API suportada pelo Hibernate 7; {@code org.hibernate.SessionFactory} não expõe {@code getJdbcServices()}).
 */
@IntegrationTest
class OracleHibernateCompatibilityIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Test
    void shouldUseOracleLegacyDialectAndRunExistsByEmailWithoutOra00933() throws Exception {
        SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
        Dialect dialect = sessionFactory.getJdbcServices().getDialect();
        assertThat(dialect).isInstanceOf(OracleLegacyDialect.class);

        String email = IntegrationTestUniqueData.colaboradorEmail("infra-be-08");
        assertThatCode(() -> colaboradorRepository.existsByEmailIgnoreCase(email))
                .doesNotThrowAnyException();
        assertThat(colaboradorRepository.existsByEmailIgnoreCase(email)).isFalse();

        writeRuntimeReport(dialect);
    }

    private void writeRuntimeReport(Dialect dialect) throws Exception {
        StringBuilder report = new StringBuilder();
        report.append("hibernate_dialect=").append(dialect.getClass().getName()).append('\n');
        try (Connection conn = dataSource.getConnection()) {
            var meta = conn.getMetaData();
            report.append("oracle_version=")
                    .append(meta.getDatabaseProductName())
                    .append(' ')
                    .append(meta.getDatabaseProductVersion())
                    .append('\n');
            report.append("jdbc_driver=")
                    .append(meta.getDriverName())
                    .append(' ')
                    .append(meta.getDriverVersion())
                    .append('\n');
        }
        try (Connection conn = dataSource.getConnection();
                PreparedStatement compatiblePs =
                        conn.prepareStatement("SELECT value FROM v$parameter WHERE name = 'compatible'");
                ResultSet compatibleRs = compatiblePs.executeQuery()) {
            if (compatibleRs.next()) {
                report.append("compatible=").append(compatibleRs.getString(1).trim()).append('\n');
            }
        } catch (Exception ignored) {
            report.append("compatible=unavailable (insufficient privilege on v$parameter)\n");
        }

        Path out = Path.of("runtime/reports/oracle-hibernate-compat.txt");
        Files.createDirectories(out.getParent());
        Files.writeString(out, report.toString());
        System.out.println(report);
    }
}
