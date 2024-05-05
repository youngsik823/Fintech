package com.ys.Fintech.exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.List;

@Slf4j
public class FieldErrorResponse {
  public static List<FieldError> getValidatedResult(BindingResult result) {
    if (result.hasErrors()) { // 입력값이 검증에 걸렸으면
      List<FieldError> fieldErrors = result.getFieldErrors();
      for (FieldError fieldError : fieldErrors) {
        log.warn("invalid client data - {}", fieldError.toString());
      }
      return fieldErrors;
    }
    return null;
  }
}
