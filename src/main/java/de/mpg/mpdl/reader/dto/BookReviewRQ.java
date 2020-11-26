package de.mpg.mpdl.reader.dto;

import de.mpg.mpdl.reader.common.Constants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/2
 */
@Data
public class BookReviewRQ {
    @NotBlank
    private String bookId;
    @NotNull
    private Constants.Rating rating;
    private String comment;
    private String name;
    private Boolean showOrg;
}
