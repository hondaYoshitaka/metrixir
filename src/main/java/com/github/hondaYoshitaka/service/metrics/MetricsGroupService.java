package com.github.hondaYoshitaka.service.metrics;

import com.github.hondaYoshitaka.model.entity.metrics.MetricsGroup;
import com.github.hondaYoshitaka.model.response.metrics.MetricsGroupCreateResponse;
import com.github.hondaYoshitaka.repository.metrics.MetricsGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MetricsGroupService {

    private final MetricsGroupDao metricsGroupDao;

    public MetricsGroupService(MetricsGroupDao metricsGroupDao) {
        this.metricsGroupDao = metricsGroupDao;
    }

    @Transactional
    public MetricsGroupCreateResponse create(final Long hostId, final Long visitorId) {
        final MetricsGroup group = new MetricsGroup();
        group.setHostId(hostId);
        group.setVisitorId(visitorId);

        metricsGroupDao.insert(group);

        return MetricsGroupCreateResponse.builder()
                .metricsGroupId(group.getId())
                .build();
    }

}
