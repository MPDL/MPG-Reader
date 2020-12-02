package de.mpg.mpdl.reader.dto;

import lombok.Data;

import java.util.HashMap;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/25
 */
@Data
public class CitationRS {
    private String bookId;
    private HashMap<String, String> citationContents;
}
