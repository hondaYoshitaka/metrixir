package example.metrixir.model.form.metrics;

import example.metrixir.model.form.FormBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class MetricsCreateForm extends FormBase {

    private ClientLocation location;

    private String name;

    private String event;

    private Long clientTime;

    @Data
    public static class ClientLocation implements Serializable {

        private String host;

        private String path;
    }
}
