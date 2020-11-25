package de.mpg.mpdl.reader.common;

import lombok.Data;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/17
 */
@Data
public class BaseResponseDTO<TContent> {

    private Integer code;

    private String message;

    private TContent content;

    public BaseResponseDTO(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
