package example.metrixir.dao.client;

import example.metrixir.configuration.DatabaseConfiguration;
import example.metrixir.model.entity.client.ClientHost;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;

import java.util.List;

@Dao(config = DatabaseConfiguration.class)
public interface ClientHostDao {
    @Select
    ClientHost findById(final Long id);

    @Select
    ClientHost findByHostAndTag(final String host, final String tag);

    @Select
    List<ClientHost> findAll();

    @Insert
    int insert(final ClientHost clientHost);
}
