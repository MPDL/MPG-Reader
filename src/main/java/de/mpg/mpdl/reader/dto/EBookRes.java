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
public class EBookRes extends BaseRS {
  private String bookId;

  private int downloads;

  private int reviews;

  private Double rating;

  private Double score;

  private boolean inReadingList;
}