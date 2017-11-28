package example.metrixir.controller.user;

import enkan.component.doma2.DomaProvider;
import example.metrixir.dao.user.VisitorDao;
import example.metrixir.model.entity.user.Visitor;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class VisitorController {
    @Inject
    private DomaProvider daoProvider;

    private VisitorDao visitorDao;

    @PostConstruct
    public void postConstruct() {
        visitorDao = daoProvider.getDao(VisitorDao.class);
    }

    public List<String> index() {
        return visitorDao.findAll().stream()
                .map(Visitor::getId)
                .collect(Collectors.toList());
    }
}
