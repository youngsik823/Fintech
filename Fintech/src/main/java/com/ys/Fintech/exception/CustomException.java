package com.ys.Fintech.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomException extends RuntimeException{
  private ErrorCode errorCode;
  private HttpStatus httpStatus;
  private String errorMessage;

  public CustomException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.httpStatus = errorCode.getHttpStatus();
    this.errorMessage = errorCode.getMessage();
  }


}
