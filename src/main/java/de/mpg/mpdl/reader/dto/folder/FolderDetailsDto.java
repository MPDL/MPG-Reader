package de.mpg.mpdl.reader.dto.folder;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author denghui.shi@safetytaxfree.com
 * @date 3/22/21
 * @desc
 */
@Data
public class FolderDetailsDto {
    private String sn;

    private String folderName;

    private List<SimpleBookDto> books = new LinkedList<>();

}
