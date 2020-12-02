package de.mpg.mpdl.reader.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasePageRequest {
    private Integer pageNumber = 0;

    private Integer pageSize = 10;

    private Sort.Direction direction = Sort.Direction.DESC;

    private String sortProperty;

    public Sort getSort() {
        if (StringUtils.hasText(sortProperty)) {
            return Sort.by(direction, sortProperty);
        }
        return Sort.unsorted();
    }
}
