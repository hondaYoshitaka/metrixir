package com.github.hondaYoshitaka.repository.host;

import com.github.hondaYoshitaka.model.entity.host.Host;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface HostDao {
    @Select
    Host findById(Long id);

    @Select
    Host findByHostAndTag(String host, String tag);

    @Select
    List<Host> findAll();

    @Insert
    int insert(Host host);
}
