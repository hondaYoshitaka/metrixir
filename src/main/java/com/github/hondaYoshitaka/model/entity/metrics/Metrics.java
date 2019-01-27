package com.github.hondaYoshitaka.model.entity.metrics;

import lombok.Data;
import org.seasar.doma.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "metrics")
@Data
public class Metrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long metricsGroupId;

    private String event;

    @Deprecated
    private String name;
    @Deprecated
    private String path;

    private LocalDateTime clientEventAt;

    private LocalDateTime createdAt;
}
