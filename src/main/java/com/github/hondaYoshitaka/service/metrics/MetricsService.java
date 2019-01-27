package com.github.hondaYoshitaka.service.metrics;

import com.github.hondaYoshitaka.model.entity.metrics.Metrics;
import com.github.hondaYoshitaka.model.entity.metrics.VisitorMetrics;
import com.github.hondaYoshitaka.model.response.metrics.HostMetricsFetchResponse;
import com.github.hondaYoshitaka.repository.metrics.MetricsDao;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.hondaYoshitaka.model.response.metrics.HostMetricsFetchResponse.MetricsDto;
import static com.github.hondaYoshitaka.model.response.metrics.HostMetricsFetchResponse.MetricsGroupDto;

@Service
@Transactional(readOnly = true)
public class MetricsService {

    private final MetricsDao metricsDao;

    public MetricsService(final MetricsDao metricsDao) {
        this.metricsDao = metricsDao;
    }

    public HostMetricsFetchResponse fetch(final Long hostId, final Pageable pageable) {
        final Map<Long, List<VisitorMetrics>> groupedMetrics = metricsDao.findAllByPage(
                hostId, pageable.getPageSize(), pageable.getOffset(),
                Collectors.groupingBy(VisitorMetrics::getMetricsGroupId));

        final HostMetricsFetchResponse response = new HostMetricsFetchResponse();

        final List<MetricsGroupDto> metricsGroups = groupedMetrics.entrySet().stream()
                .map(entry -> {
                    final MetricsGroupDto dto = new MetricsGroupDto();
                    dto.setId(entry.getKey());

                    final List<MetricsDto> metricsList = entry.getValue().stream()
                            .map(this::createMetricsDto)
                            .collect(Collectors.toList());
                    dto.setMetrics(metricsList);

                    return dto;
                })
                .collect(Collectors.toList());
        response.setGroups(metricsGroups);

        return response;
    }

    @Transactional
    public void save(final Long metricsGroupId,
                     final String event,
                     final String path,
                     final String name,
                     final LocalDateTime clientTime
    ) {
        // TODO: exist metricsGroup?

        final Metrics metrics = new Metrics();
        metrics.setMetricsGroupId(metricsGroupId);
        metrics.setEvent(event);
        metrics.setPath(path);
        metrics.setName(name);
        metrics.setClientEventAt(clientTime);
        metrics.setCreatedAt(LocalDateTime.now());

        metricsDao.insert(metrics);
    }

    private MetricsDto createMetricsDto(final Metrics metrics) {
        final MetricsDto metricsDto = new MetricsDto();

        metricsDto.setId(metrics.getId());
        metricsDto.setEvent(metrics.getEvent());
        metricsDto.setPath(metrics.getPath());
        metricsDto.setName(metrics.getName());
        metricsDto.setClientEventAt(metrics.getClientEventAt());

        return metricsDto;
    }
}
