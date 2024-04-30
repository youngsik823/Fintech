package com.ys.Fintech.accountUser.api;

import com.ys.Fintech.accountUser.dto.request.SignInRequestDTO;
import com.ys.Fintech.accountUser.dto.request.SignUpRequestDTO;
import com.ys.Fintech.accountUser.dto.response.SignInResponseDTO;
import com.ys.Fintech.accountUser.dto.response.SignUpResponseDTO;
import com.ys.Fintech.accountUser.service.AccountUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    ResponseEntity<List<FieldError>> filedErrors = getValidatedResult(result);
    if (filedErrors != null) {  // valid 검증을 했을때 에러가 있는지 확인
      return filedErrors;
    }
    try {
      SignUpResponseDTO signUpResponseDTO = accountUserService.SignUp(signUpRequestDTO);
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
  public ResponseEntity<?>signIn(
      @RequestBody @Validated SignInRequestDTO signInRequestDTO
      , BindingResult result
      ) {
    ResponseEntity<List<FieldError>> validatedResult = getValidatedResult(result);
    if (validatedResult != null) {
      return validatedResult;
    }
    SignInResponseDTO signInResponseDTO = accountUserService.verifyLogin(signInRequestDTO);
    try {
      return ResponseEntity.ok().body(signInResponseDTO);
    } catch (RuntimeException e) {
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
