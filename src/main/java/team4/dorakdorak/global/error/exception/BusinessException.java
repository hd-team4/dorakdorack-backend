package team4.dorakdorak.global.error.exception;

import lombok.Getter;
import team4.dorakdorak.global.error.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(final String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
