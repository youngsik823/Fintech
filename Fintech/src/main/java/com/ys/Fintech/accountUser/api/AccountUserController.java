package com.ys.Fintech.accountUser.api;

import com.ys.Fintech.accountUser.dto.request.AccountUserModifyRequestDTO;
import com.ys.Fintech.accountUser.dto.request.SignInRequestDTO;
import com.ys.Fintech.accountUser.dto.request.SignUpRequestDTO;
import com.ys.Fintech.accountUser.dto.response.AccountUserModifyResponseDTO;
import com.ys.Fintech.accountUser.dto.response.SignInResponseDTO;
import com.ys.Fintech.accountUser.dto.response.SignUpResponseDTO;
import com.ys.Fintech.accountUser.service.AccountUserService;
import com.ys.Fintech.security.TokenAccountUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/accountUser")
public class AccountUserController {
  private final AccountUserService accountUserService;

  // 회원가입
  @PostMapping("/signUp")
  public ResponseEntity<?> signUp(
      @RequestBody @Validated SignUpRequestDTO signUpRequestDTO
      , BindingResult result
  ) {

    try {
    ResponseEntity<List<FieldError>> filedErrors = getValidatedResult(result);
    if (filedErrors != null) {  // valid 검증을 했을때 에러가 있는지 확인
      return filedErrors;
    }
      SignUpResponseDTO signUpResponseDTO = accountUserService.signUp(signUpRequestDTO);

      return ResponseEntity
          .ok()
          .body(signUpResponseDTO);
    } catch (Exception e) {
      return ResponseEntity
          .badRequest()
          .body(e.getMessage());
    }
  }

  // 로그인
  @PostMapping("/signIn")
  public ResponseEntity<?> signIn(
      @RequestBody @Validated SignInRequestDTO signInRequestDTO
      , BindingResult result
  ) {
    try {
      ResponseEntity<List<FieldError>> validatedResult = getValidatedResult(result);
      if (validatedResult != null) {
        return validatedResult;
      }
      SignInResponseDTO signInResponseDTO = accountUserService.verifyLogin(signInRequestDTO);
      log.info("signInResponseDTO  -  {}", signInResponseDTO);
      return ResponseEntity.ok().body(signInResponseDTO);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 회원가입 수정
  @RequestMapping(value = "/modify", method = {RequestMethod.PUT, RequestMethod.PATCH})
  public ResponseEntity<?> modifyAccountUser(
      @RequestBody @Validated AccountUserModifyRequestDTO accountUserModifyRequestDTO
      , @AuthenticationPrincipal TokenAccountUserInfo accountUserInfo
      , BindingResult result
  ) {
    log.info("tokenInfo ----------------{}", accountUserInfo);
    log.info("accountUserModify ----------------{}", accountUserModifyRequestDTO);
    try {
      ResponseEntity<List<FieldError>> validatedResult = getValidatedResult(result);
      if (validatedResult != null) {
        return validatedResult;
      }
      AccountUserModifyResponseDTO accountUserModifyResponseDTO
          = accountUserService.accountUserModify(accountUserModifyRequestDTO, accountUserInfo);

      return ResponseEntity.ok().body(accountUserModifyResponseDTO);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  private static ResponseEntity<List<FieldError>> getValidatedResult(BindingResult result) {
    if (result.hasErrors()) { // 입력값이 검증에 걸렸으면
      List<FieldError> fieldErrors = result.getFieldErrors();
      for (FieldError fieldError : fieldErrors) {
        log.warn("invalid client data - {}", fieldError.toString());
      }
      return ResponseEntity
          .badRequest()
          .body(fieldErrors);
    }
    return null;
  }


}
