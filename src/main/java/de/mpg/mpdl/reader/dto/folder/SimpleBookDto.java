package de.mpg.mpdl.reader.dto.folder;

import lombok.Data;

/**
 * @author denghui.shi@safetytaxfree.com
 * @date 4/13/21
 * @desc
 */
@Data
public class SimpleBookDto {
    private String bookId;

    private String bookName;

    private String bookCoverURL;
}
