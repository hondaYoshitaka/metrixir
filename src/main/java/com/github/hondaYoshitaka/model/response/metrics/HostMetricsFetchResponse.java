package com.github.hondaYoshitaka.model.response.metrics;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HostMetricsFetchResponse {

    @ApiModelProperty(name = "メトリクスグループ一覧", required = true)
    private List<MetricsGroupDto> groups;

    @Data
    public static class MetricsGroupDto {

        @ApiModelProperty(name = "ID", required = true)
        private Long id;

        @ApiModelProperty(name = "メトリクス一覧", required = true)
        private List<MetricsDto> metrics;
    }

    @Data
    public static class MetricsDto {

        @ApiModelProperty(name = "ID", required = true)
        private Long id;

        @ApiModelProperty(name = "イベント名", required = true)
        private String event;

        @ApiModelProperty(name = "項目名", required = true)
        private String name;

        @ApiModelProperty(name = "パス", required = true)
        private String path;

        @ApiModelProperty(name = "クライアント時刻", required = true)
        private LocalDateTime clientEventAt;
    }
}
