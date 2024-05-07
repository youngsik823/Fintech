package com.ys.Fintech.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  // ErrorCode 내의 에러
  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(CustomException e) {
    log.error("customException is occurred. ", e);
    return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getErrorCode(), e.getErrorCode().getCode(), e.getHttpStatus(), e.getErrorMessage()));
  }

}
