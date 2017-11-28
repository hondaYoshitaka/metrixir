package example.metrixir.model.entity.user;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
@Data
public class Visitor {
    @Id
    private String id;
}
