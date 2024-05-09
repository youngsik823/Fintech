package com.ys.Fintech.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  /**
   * ******************************* Global Error CodeList ***************************************
   * HTTP Status Code
   * 400 : Bad Request
   * 401 : Unauthorized
   * 403 : Forbidden
   * 404 : Not Found
   * 500 : Internal Server Error
   * *********************************************************************************************
   */

  // 유저
  // 잘못된 서버 요청

  // Request dto 로 데이터가 전달되지 않을 경우
  MISSING_REQUEST_DTO_ERROR(HttpStatus.NOT_FOUND, "I-001", "DTO 값이 없습니다."),

  // ACCOUNT_USER
  EXISTS_USER_EMAIL_OR_PHONE_NUM(HttpStatus.BAD_REQUEST, "U-001", "이메일 또는 핸드폰번호가 존재합니다."),
  NOT_EXISTS_USER_EMAIL(HttpStatus.NOT_FOUND, "U-002", "이메일이 존재하지 않습니다."),
  NOT_ACCORD_USER_PASSWORD(HttpStatus.NOT_FOUND, "U-003", "패스워드가 일치하지 않습니다."),
  NOT_ACCORD_USER_EMAIL(HttpStatus.NOT_FOUND, "U-004", "이메일이 일치하지 않습니다."),
  NOT_EXISTS_USER(HttpStatus.NOT_FOUND, "U-005", "회원이 존재하지 않습니다."),

  // ACCOUNT
  EXCEED_ACCOUNT_NUM(HttpStatus.BAD_REQUEST, "A-001", "이미 계좌가 5개 존재합니다."),
  NOT_EXISTS_ACCOUNT(HttpStatus.NOT_FOUND, "A-002", "계좌가 존재하지 않습니다."),
  EXISTS_ACCOUNT_AMOUNT(HttpStatus.BAD_REQUEST, "A-003", "잔액이 남아 있습니다."),
  ALREADY_ACCOUNT_STATUS_UN_USED(HttpStatus.BAD_REQUEST, "A-004", "계좌가 이미 삭제 상태입니다."),

  // Transaction
  NOT_ENOUGH_AMOUNT(HttpStatus.BAD_REQUEST, "T-001", "거래 잔액이 부족합니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  }
