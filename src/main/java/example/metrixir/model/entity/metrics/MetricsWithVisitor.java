package example.metrixir.model.entity.metrics;

import lombok.Data;
import org.seasar.doma.Entity;

@Entity
@Data
public class MetricsWithVisitor extends Metrics {

    private String visitorId;
}
