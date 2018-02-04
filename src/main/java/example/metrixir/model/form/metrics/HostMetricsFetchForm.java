package example.metrixir.model.form.metrics;

import example.metrixir.model.form.FormBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HostMetricsFetchForm extends FormBase {

    private Long hostId;

    private String path;
}
