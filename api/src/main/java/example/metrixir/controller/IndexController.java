package example.metrixir.controller;

import enkan.component.doma2.DomaProvider;
import enkan.data.HttpResponse;
import example.metrixir.dao.client.ClientHostDao;
import kotowari.component.TemplateEngine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class IndexController {
    @Inject
    private DomaProvider daoProvider;
    @Inject
    private TemplateEngine templateEngine;

    private ClientHostDao clientHostDao;

    @PostConstruct
    public void init() {
        clientHostDao = daoProvider.getDao(ClientHostDao.class);
    }

    /**
     * @return view
     */
    public HttpResponse index() {
        final Object[] params = new Object[]{"clientHosts", clientHostDao.findAll()};

        return templateEngine.render("index", params);
    }
}
