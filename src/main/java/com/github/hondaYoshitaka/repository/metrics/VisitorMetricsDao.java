package com.github.hondaYoshitaka.repository.metrics;

import com.github.hondaYoshitaka.model.entity.metrics.VisitorMetrics;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface VisitorMetricsDao {
    @Insert
    int insert(VisitorMetrics entity);
}
