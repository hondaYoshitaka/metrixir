package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.Statement;

public class V2__CreateVisitor implements JdbcMigration {

    @Override
    public void migrate(Connection connection) throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE visitor (" +
                    "  id                VARCHAR(255) PRIMARY KEY " +
                    ")");

            stmt.execute("CREATE TABLE visitor_metrics (" +
                    "  visitor_id                VARCHAR(255) NOT NULL," +
                    "  metrics_id                BIGINT NOT NULL," +
                    "  FOREIGN KEY (visitor_id) REFERENCES visitor(id)," +
                    "  FOREIGN KEY (metrics_id) REFERENCES metrics(id)" +
                    ")");
        }
    }
}
