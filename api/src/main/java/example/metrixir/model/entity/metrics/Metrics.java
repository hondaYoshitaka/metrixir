package example.metrixir.model.entity.metrics;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Metrics {

    private String event;

    private String name;

    private String host;

    private String path;

    private LocalDateTime createdAt;
}
