package de.mpg.mpdl.reader.dto;

import de.mpg.mpdl.reader.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author shidenghui@gmail.com
 * @date 2020/12/11
 */
@Data
@AllArgsConstructor
public class CitationDTO {
    private Constants.CitationType type;
    private String value;
}
