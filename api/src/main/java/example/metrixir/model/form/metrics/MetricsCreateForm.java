package example.metrixir.model.form.metrics;

import example.metrixir.model.form.FormBase;
import lombok.Data;

import java.util.Map;

@Data
public class MetricsCreateForm extends FormBase {

    private Map<String, String> location;

    private String name;

    private String event;

    private String clientTime;
}
