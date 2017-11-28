package example.metrixir.model.entity.metrics;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;

import java.time.LocalDateTime;

@Entity
@Data
public class Metrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String event;

    private String name;

    private String host;

    private String path;

    private LocalDateTime clientEventAt;

    private LocalDateTime createdAt;
}
