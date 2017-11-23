package example.metrixir.dao.metrics;

import example.metrixir.configuration.DatabaseConfiguration;
import example.metrixir.model.entity.metrics.Metrics;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;

@Dao(config = DatabaseConfiguration.class)
public interface MetricsDao {
    @Insert
    int insert(final Metrics entity);
}
