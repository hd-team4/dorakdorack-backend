package dorakdorak.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import dorakdorak.global.error.exception.BusinessException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
    log.error("handleBusinessException", e);
    final ErrorCode errorCode = e.getErrorCode();
    final ErrorResponse errorResponse = ErrorResponse.of(errorCode);
    return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatus()));
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(final Exception e) {
    log.error("handleException", e);
    final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(DuplicateKeyException.class)
  protected ResponseEntity<ErrorResponse> handleDuplicateKeyException(
      final DuplicateKeyException e) {
    log.error("handleDuplicateKeyException", e);
    final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.DUPLICATED_EMAIL);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
