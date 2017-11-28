package example.metrixir.model.response.metrics;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MetricsResponseDto implements Serializable {

    private Map<String, List<MetricsDto>> metricsMap = new HashMap<>();

    @Data
    @AllArgsConstructor
    public static class MetricsDto implements Serializable {

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        private LocalDateTime start;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        private LocalDateTime end;
    }
}
