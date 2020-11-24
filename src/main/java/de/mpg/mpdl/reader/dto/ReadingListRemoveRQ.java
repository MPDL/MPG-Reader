package de.mpg.mpdl.reader.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author denghui.shi@safetytaxfree.com
 * @date 2020/11/24
 */
@Data
public class ReadingListRemoveRQ {
    @NotNull
    private List<String> bookIds;
}
