package example.metrixir.controller.metrics;

import enkan.component.doma2.DomaProvider;
import example.metrixir.model.form.metrics.MetricsCreateForm;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class MetricsController {
    @Inject
    private DomaProvider daoProvider;

    /**
     * @param form
     */
    @Transactional
    public void create(final MetricsCreateForm form) {
        // formを経由するとbodyが解析できない...
        System.out.println(form); // out: -
    }
}
