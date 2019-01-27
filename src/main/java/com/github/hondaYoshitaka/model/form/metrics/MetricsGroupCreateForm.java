package com.github.hondaYoshitaka.model.form.metrics;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MetricsGroupCreateForm {
    @NotNull
    @ApiModelProperty(name = "ホストID", required = true)
    private Long hostId;

    @NotNull
    @ApiModelProperty(name = "訪問者ID", required = true)
    private Long visitorId;
}
