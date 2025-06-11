package dorakdorak.infra.openai.exception;

import dorakdorak.global.error.ErrorCode;
import dorakdorak.global.error.exception.BusinessException;

public class OpenAiVisionException extends BusinessException {

    public OpenAiVisionException(String message) {
        super(message, ErrorCode.OPENAI_VISION_ERROR);
    }

    public OpenAiVisionException(String message, Throwable cause) {
        super(message, ErrorCode.OPENAI_VISION_ERROR);
        initCause(cause);
    }
}
