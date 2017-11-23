package example.metrixir.controller.metrics;

import enkan.component.doma2.DomaProvider;
import example.metrixir.model.form.metrics.MetricsCreateForm;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class MetricsController {
    @Inject
    private DomaProvider daoProvider;

    public void preflight() {
    }

    @Transactional
    public void create(final MetricsCreateForm form) {
        System.out.println(form);
    }
}
