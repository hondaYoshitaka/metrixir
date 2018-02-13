package example.metrixir.dao.metrics;

import example.metrixir.configuration.DatabaseConfiguration;
import example.metrixir.model.entity.metrics.Metrics;
import example.metrixir.model.entity.metrics.MetricsWithVisitor;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

@Dao(config = DatabaseConfiguration.class)
public interface MetricsDao {
    @Select
    List<MetricsWithVisitor> findAllByPage(final Long hostId,
                                           final int limit,
                                           final int offset);

    @Select
    int countByHostId(final Long hostId);

    @Insert
    int insert(final Metrics entity);
}
