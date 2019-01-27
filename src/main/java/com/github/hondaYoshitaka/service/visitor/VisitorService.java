package com.github.hondaYoshitaka.service.visitor;

import com.github.hondaYoshitaka.model.entity.visitor.Visitor;
import com.github.hondaYoshitaka.model.response.visitor.VisitorCreateResponse;
import com.github.hondaYoshitaka.repository.visitor.VisitorDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VisitorService {

    private final VisitorDao visitorDao;

    public VisitorService(final VisitorDao visitorDao) {
        this.visitorDao = visitorDao;
    }

    @Transactional
    public VisitorCreateResponse create(final Long hostId) {
        final Visitor visitor = new Visitor();
        visitor.setHostId(hostId);

        visitorDao.insert(visitor);

        return VisitorCreateResponse.builder()
                .visitorId(visitor.getId())
                .build();
    }
}
