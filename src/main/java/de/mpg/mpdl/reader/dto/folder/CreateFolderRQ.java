package de.mpg.mpdl.reader.dto.folder;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author denghui.shi@safetytaxfree.com
 * @date 3/22/21
 * @desc
 */
@Data
public class CreateFolderRQ {
    @NotBlank
    private String folderName;
}
