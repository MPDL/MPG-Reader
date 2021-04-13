package de.mpg.mpdl.reader.dto.folder;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * @author denghui.shi@safetytaxfree.com
 * @date 3/22/21
 * @desc
 */
@Data
public class MoveInBooksRQ {
    @NotBlank
    private String destFolderName;

    @NotEmpty
    private Set<String> bookIds;
}