package example.metrixir.dao.metrics;

import example.metrixir.configuration.DatabaseConfiguration;
import example.metrixir.model.entity.metrics.VisitorMetrics;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;

@Dao(config = DatabaseConfiguration.class)
public interface VisitorMetricsDao {
    @Insert
    int insert(final VisitorMetrics entity);
}
