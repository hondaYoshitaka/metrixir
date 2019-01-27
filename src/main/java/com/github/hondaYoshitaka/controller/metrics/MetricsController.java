package com.github.hondaYoshitaka.controller.metrics;

import com.github.hondaYoshitaka.model.form.metrics.MetricsCreateForm;
import com.github.hondaYoshitaka.model.response.metrics.HostMetricsFetchResponse;
import com.github.hondaYoshitaka.service.metrics.MetricsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("api")
public class MetricsController {

    private MetricsService metricsService;

    public MetricsController(final MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @ApiOperation("ホストのmetrics一覧を取得します.")
    @GetMapping("hosts/{hostId}/page_metrics")
    public HostMetricsFetchResponse fetchHostMetrics(
            @PathVariable final Long hostId,
            final Pageable pageable
    ) {
        return metricsService.fetch(hostId, pageable);
    }

    @ApiOperation("metricを登録します.")
    @PostMapping("metrics")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void create(
            @RequestBody @Validated final MetricsCreateForm form
    ) {
        final Instant instant = Instant.ofEpochMilli(form.getClientTime());
        final LocalDateTime clientTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        metricsService.save(form.getMetricsGroupId(), form.getEvent(), form.getPath(), form.getName(), clientTime);
    }
}
