package example.metrixir.configuration;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;

import javax.sql.DataSource;

public class DatabaseConfiguration implements Config {
    @Override
    public DataSource getDataSource() {
        return null;
    }

    @Override
    public Naming getNaming() {
        return Naming.SNAKE_UPPER_CASE;
    }

    @Override
    public Dialect getDialect() {
        return new H2Dialect();
    }
}
