package com.github.hondaYoshitaka.model.response.visitor;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VisitorCreateResponse {

    @ApiModelProperty(name = "訪問者ID", required = true)
    private Long visitorId;
}
