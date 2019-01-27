package com.github.hondaYoshitaka.model.response.host;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HostCreateResponse {

    @ApiModelProperty(name = "ホストID", required = true)
    private Long hostId;
}
