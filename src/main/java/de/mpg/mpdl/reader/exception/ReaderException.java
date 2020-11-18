package de.mpg.mpdl.reader.exception;

import de.mpg.mpdl.reader.common.ResponseBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ReaderException extends RuntimeException {

    private ResponseBuilder.RetCode retCode;

    public ReaderException(ResponseBuilder.RetCode retCode) {
        this.retCode = retCode;
    }

    public ReaderException(ResponseBuilder.RetCode retCode, String message) {
        super(message);
        this.retCode = retCode;
    }
}
