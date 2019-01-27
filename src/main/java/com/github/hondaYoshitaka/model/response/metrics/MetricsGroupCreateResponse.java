package com.github.hondaYoshitaka.model.response.metrics;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MetricsGroupCreateResponse {

    @ApiModelProperty(name = "メトリクスグループID", required = true)
    private Long metricsGroupId;
}
