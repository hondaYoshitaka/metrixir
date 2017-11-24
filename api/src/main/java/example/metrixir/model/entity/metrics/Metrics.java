package example.metrixir.model.entity.metrics;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

import java.time.LocalDateTime;

@Data
@Entity
public class Metrics {
    @Id
    private Long id;

    private String event;

    private String name;

    private String host;

    private String path;

    private LocalDateTime createdAt;
}
