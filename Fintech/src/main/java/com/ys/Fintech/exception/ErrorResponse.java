package com.ys.Fintech.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

  private ErrorCode errorCode;
  private String code;
  private HttpStatus httpStatus;
  private String errorMessage;
  private String timeStamp;


  public ErrorResponse(ErrorCode errorCode, String code, HttpStatus httpStatus, String message) {
    this.errorCode = errorCode;
    this.code = code;
    this.httpStatus = httpStatus;
    this.errorMessage = message;
    this.timeStamp = String.valueOf(LocalDateTime.now());
  }
}
