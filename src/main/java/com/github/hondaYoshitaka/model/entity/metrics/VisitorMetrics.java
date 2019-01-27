package com.github.hondaYoshitaka.model.entity.metrics;


import lombok.Data;
import org.seasar.doma.Entity;

@Entity
@Data
public class VisitorMetrics extends Metrics {

    private Long visitorId;
}
