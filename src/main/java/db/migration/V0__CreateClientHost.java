package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.Statement;

public class V0__CreateClientHost implements JdbcMigration {

    @Override
    public void migrate(Connection connection) throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE client_host (" +
                    "  id                SERIAL PRIMARY KEY," +
                    "  host              VARCHAR(255) NOT NULL," +
                    "  tag               VARCHAR(255)" +
                    ")");
        }
    }
}
