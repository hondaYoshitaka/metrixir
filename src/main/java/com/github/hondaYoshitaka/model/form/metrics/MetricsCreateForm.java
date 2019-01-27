package com.github.hondaYoshitaka.model.form.metrics;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MetricsCreateForm {

    @NotNull
    @ApiModelProperty(name = "メトリクスグループID", required = true)
    private Long metricsGroupId;

    @NotNull
    @ApiModelProperty(name = "入力要素名", required = true)
    private String name;

    @NotNull
    @ApiModelProperty(name = "イベント名", required = true)
    private String event;

    @ApiModelProperty(name = "uriパス", required = true)
    private String path;

    @NotNull
    @ApiModelProperty(name = "クライアント時刻", required = true)
    private Long clientTime;
}
