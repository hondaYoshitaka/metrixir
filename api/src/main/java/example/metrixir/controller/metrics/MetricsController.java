package example.metrixir.controller.metrics;

import enkan.component.doma2.DomaProvider;
import enkan.data.HttpRequest;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MetricsController {
    @Inject
    private DomaProvider daoProvider;

    public void preflight() {
    }

    /**
     * curl -X POST -H 'Content-Type:application/json' -d '{"location":{"host":"www.example.com"}, "name":"lastName", "action":"focus"}' 'http://localhost:3000/api/metrics'
     *
     * @param form
     */
    @Transactional
    public void create(
//            final MetricsCreateForm form,
            final HttpRequest request
    ) {
        // 1. formを経由するとbodyが解析できない...
//        System.out.println(form); // out: -

        // 2. bodyを直接プリントすると上手く行く
        try (final InputStream is = request.getBody()) {
            final List<String> lines = IOUtils.readLines(is, StandardCharsets.UTF_8);

            lines.forEach(System.out::println); // out: {"location":{"host":"www.example.com"}, "name":"lastName", "action":"focus"}

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
