package br.com.unimedceara.portalcomunicacao.infrastructure.observability.health;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Verifica conectividade com o banco de dados Oracle.
 */
@Component("db")
@ConditionalOnBean(DataSource.class)
public class DatabaseHealthIndicator implements HealthIndicator {

    private static final int VALIDATION_TIMEOUT_SECONDS = 2;
    private static final String DATABASE_NAME = "Oracle";

    private final DataSource dataSource;

    /**
     * Constrói o health indicator do banco de dados.
     *
     * @param dataSource datasource primário da aplicação
     */
    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(VALIDATION_TIMEOUT_SECONDS)) {
                return Health.up()
                        .withDetail("database", DATABASE_NAME)
                        .build();
            }
            return Health.down()
                    .withDetail("database", DATABASE_NAME)
                    .withDetail("error", "connection validation failed")
                    .build();
        } catch (SQLException ex) {
            return Health.down(ex)
                    .withDetail("database", DATABASE_NAME)
                    .build();
        }
    }
}
