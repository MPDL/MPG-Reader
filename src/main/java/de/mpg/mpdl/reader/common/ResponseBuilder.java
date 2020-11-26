package de.mpg.mpdl.reader.common;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/17
 */
public class ResponseBuilder {
    public enum RetCode {

        /**
         * Success response
         */
        SUCCESS_0(0, "ok"),

        /**
         * Invalid parameter!
         */
        ERROR_400000(400000, "illegal parameter"),

        ERROR_400002(400002, "duplicate review"),

        ERROR_400003(400003, "no citations are available"),

        /**
         * Server internal error response with code 50003.
         */
        ERROR_500003(500003, "service unavailable now, try later.");


        private int code;
        private String message;

        RetCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return this.code;
        }

        public String getMessage() {
            return this.message;
        }
    }

    public static <TContent> BaseResponseDTO<TContent> buildSuccess(TContent t) {
        BaseResponseDTO<TContent> baseResponseDTO = buildSuccess();
        baseResponseDTO.setContent(t);
        return baseResponseDTO;
    }

    public static <TContent> BaseResponseDTO<TContent> buildSuccess() {
        return buildCommon(RetCode.SUCCESS_0);
    }

    public static <TContent> BaseResponseDTO<TContent> buildCommon(RetCode code) {
        return buildErrorCommon(code.code, code.message);
    }

    public static <TContent> BaseResponseDTO<TContent> buildErrorCommon(RetCode code, String message) {
        return buildErrorCommon(code.code, message);
    }

    public static <TContent> BaseResponseDTO<TContent> buildErrorCommon(Integer code, String message) {
        return new BaseResponseDTO<>(code, message);
    }
}
