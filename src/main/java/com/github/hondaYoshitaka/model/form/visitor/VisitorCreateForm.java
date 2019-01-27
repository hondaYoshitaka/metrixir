package com.github.hondaYoshitaka.model.form.visitor;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VisitorCreateForm {

    @NotNull
    @ApiModelProperty(name = "ホストID", required = true)
    private Long hostId;
}
