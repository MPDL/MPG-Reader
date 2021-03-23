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
public class BookshelfDto {
    private String sn;
    private List<String> folderNames = new LinkedList<>();
    private List<String> bookIds = new LinkedList<>();
}
