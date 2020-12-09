package de.mpg.mpdl.reader.dto;

import de.mpg.mpdl.reader.common.BaseRS;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/2
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class EBookStatisticRes extends BaseRS {
  private String bookId;

  private String bookName;

  private String bookCoverURL;

  private int downloads;

  private int reviews;

  private Double rating;

  private boolean inReadingList = false;

  private boolean isReviewedByMe = false;
}