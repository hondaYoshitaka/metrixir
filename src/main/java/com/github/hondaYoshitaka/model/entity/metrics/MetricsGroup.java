package com.github.hondaYoshitaka.model.entity.metrics;

import lombok.Data;
import org.seasar.doma.*;

@Entity
@Table(name = "metrics_groups")
@Data
public class MetricsGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long hostId;

    private Long visitorId;
}
