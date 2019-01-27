package com.github.hondaYoshitaka.repository.metrics;

import com.github.hondaYoshitaka.model.entity.metrics.Metrics;
import com.github.hondaYoshitaka.model.entity.metrics.VisitorMetrics;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.stream.Collector;

@Dao
@ConfigAutowireable
public interface MetricsDao {

    @Select(strategy = SelectType.COLLECT)
    <RESULT> RESULT findAllByPage(Long hostId,
                                  long limit,
                                  long offset,
                                  Collector<VisitorMetrics, ?, RESULT> collector);

    @Select
    int countByHostId(Long hostId);

    @Select
    int countAll();

    @Insert
    int insert(Metrics entity);
}
