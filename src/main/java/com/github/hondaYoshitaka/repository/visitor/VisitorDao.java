package com.github.hondaYoshitaka.repository.visitor;

import com.github.hondaYoshitaka.model.entity.visitor.Visitor;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface VisitorDao {
    @Select
    Visitor findOne(String id);

    @Insert
    int insert(Visitor visitor);
}
