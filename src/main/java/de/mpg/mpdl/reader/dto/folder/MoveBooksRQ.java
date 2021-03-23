package de.mpg.mpdl.reader.dto.folder;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author denghui.shi@safetytaxfree.com
 * @date 3/22/21
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MoveBooksRQ extends MoveOutBooksRQ{
    @NotBlank
    private String destFolderName;

}
