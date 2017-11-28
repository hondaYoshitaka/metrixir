package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.Statement;

public class V1__CreateMetrics implements JdbcMigration {

    @Override
    public void migrate(Connection connection) throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE metrics (" +
                    "  id                BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "  event             VARCHAR(255) NOT NULL," +
                    "  name              VARCHAR(255) NOT NULL," +
                    "  host              VARCHAR(255) NOT NULL," +
                    "  path              VARCHAR(512) NOT NULL," +
                    "  client_event_at   TIMESTAMP," +
                    "  created_at        TIMESTAMP" +
                    ")");
        }
    }
}
