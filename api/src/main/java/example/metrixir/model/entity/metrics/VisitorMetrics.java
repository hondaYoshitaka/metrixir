package example.metrixir.model.entity.metrics;

import lombok.Data;
import org.seasar.doma.Entity;

@Entity
@Data
public class VisitorMetrics {
    private String visitorId;

    private Long metricsId;
}
