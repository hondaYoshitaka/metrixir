package com.github.hondaYoshitaka.model.form.host;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class HostCreateForm {

    @NotNull
    @ApiModelProperty(name = "ホスト名", required = true)
    private String hostName;

    @ApiModelProperty(name = "タグ")
    private String tag;
}
