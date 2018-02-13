package example.metrixir.component.web.pager;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author y_honda_
 */
@Data
@AllArgsConstructor
public class Pagination {

    private Integer total = 0;

    private Integer page = 0;

    private Integer limit = 1;

    public Integer getOffset() {
        return page * limit;
    }
}
