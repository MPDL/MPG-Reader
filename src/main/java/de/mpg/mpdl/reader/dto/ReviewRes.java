package de.mpg.mpdl.reader.dto;

import de.mpg.mpdl.reader.common.BaseRS;
import de.mpg.mpdl.reader.common.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/2
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ReviewRes extends BaseRS {
  private Constants.Rating rating;

  private String comment;

  private String userName;

}