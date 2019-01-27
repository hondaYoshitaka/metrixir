package com.github.hondaYoshitaka.repository.metrics;

import com.github.hondaYoshitaka.model.entity.metrics.MetricsGroup;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface MetricsGroupDao {

    @Insert
    int insert(MetricsGroup entity);
}
