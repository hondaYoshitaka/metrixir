package example.metrixir.dao.metrics;

import example.metrixir.configuration.DatabaseConfiguration;
import example.metrixir.model.entity.metrics.Metrics;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;

import java.util.List;

@Dao(config = DatabaseConfiguration.class)
public interface MetricsDao {

    @Select
    List<Metrics> findVisitorMetricsByTag(final String visitorId, final String tag);

    @Select
    List<Metrics> findAllByPage(final Long hostId);

    @Insert
    int insert(final Metrics entity);
}
