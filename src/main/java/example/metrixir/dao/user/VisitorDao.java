package example.metrixir.dao.user;

import example.metrixir.configuration.DatabaseConfiguration;
import example.metrixir.model.entity.user.Visitor;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;

import java.util.List;

@Dao(config = DatabaseConfiguration.class)
public interface VisitorDao {
    @Select
    Visitor findOne(final String id);

    @Select
    List<Visitor> findAllByTag(String tag);

    @Select
    List<Visitor> findAll();

    @Insert
    int insert(final Visitor visitor);

}
