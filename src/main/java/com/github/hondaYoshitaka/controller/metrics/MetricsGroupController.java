package com.github.hondaYoshitaka.controller.metrics;

import com.github.hondaYoshitaka.model.form.metrics.MetricsGroupCreateForm;
import com.github.hondaYoshitaka.model.response.metrics.MetricsGroupCreateResponse;
import com.github.hondaYoshitaka.service.metrics.MetricsGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class MetricsGroupController {

    private final MetricsGroupService metricsGroupService;

    public MetricsGroupController(final MetricsGroupService metricsGroupService) {
        this.metricsGroupService = metricsGroupService;
    }

    @PostMapping("metrics_groups")
    @ResponseStatus(code = HttpStatus.CREATED)
    public MetricsGroupCreateResponse create(
            @RequestBody @Validated final MetricsGroupCreateForm form
    ) {
        return metricsGroupService.create(form.getHostId(), form.getVisitorId());
    }
}
