package com.github.hondaYoshitaka.service.host;

import com.github.hondaYoshitaka.model.entity.host.Host;
import com.github.hondaYoshitaka.model.response.host.HostCreateResponse;
import com.github.hondaYoshitaka.repository.host.HostDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HostService {

    private final HostDao hostDao;

    public HostService(HostDao hostDao) {
        this.hostDao = hostDao;
    }

    @Transactional
    public HostCreateResponse create(final String hostName, final String tag) {
        final Host host = new Host();
        host.setHost(hostName);
        host.setTag(tag);

        hostDao.insert(host);

        return HostCreateResponse.builder()
                .hostId(host.getId())
                .build();
    }
}
