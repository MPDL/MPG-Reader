package de.mpg.mpdl.reader.dto;

import de.mpg.mpdl.reader.common.BaseRS;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/2
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ReadingListRes extends BaseRS {
    private String sn;
    private List<String> bookIds;
}