package com.ys.Fintech.accountUser.api;

import com.ys.Fintech.accountUser.dto.request.DeleteAccountUserRequestDTO;
import com.ys.Fintech.accountUser.dto.request.ModifyAccountUserRequestDTO;
import com.ys.Fintech.accountUser.dto.request.SignInRequestDTO;
import com.ys.Fintech.accountUser.dto.request.SignUpRequestDTO;
import com.ys.Fintech.accountUser.dto.response.ModifyAccountUserResponseDTO;
import com.ys.Fintech.accountUser.dto.response.SignInResponseDTO;
import com.ys.Fintech.accountUser.dto.response.SignUpResponseDTO;
import com.ys.Fintech.accountUser.service.AccountUserService;
import com.ys.Fintech.exception.FieldErrorResponse;
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

      List<FieldError> validatedResult = FieldErrorResponse.getValidatedResult(result);
      if (validatedResult != null) {  // valid 검증을 했을때 에러가 있는지 확인
      return ResponseEntity.badRequest().body(validatedResult);
    }
      SignUpResponseDTO signUpResponseDTO = accountUserService.signUp(signUpRequestDTO);

      return ResponseEntity
          .ok()
          .body(signUpResponseDTO);
    }

  // 로그인
  @PostMapping("/signIn")
  public ResponseEntity<?> signIn(
      @RequestBody @Validated SignInRequestDTO signInRequestDTO
      , BindingResult result
  ) {
      List<FieldError> validatedResult = FieldErrorResponse.getValidatedResult(result);
      if (validatedResult != null) {
        return ResponseEntity.badRequest().body(validatedResult);
      }

      SignInResponseDTO signInResponseDTO = accountUserService.verifyLogin(signInRequestDTO);
      log.info("signInResponseDTO  -  {}", signInResponseDTO);
      return ResponseEntity.ok().body(signInResponseDTO);
    }


  // 회원가입 수정
  @RequestMapping(value = "/modify", method = {RequestMethod.PUT, RequestMethod.PATCH})
  public ResponseEntity<?> modifyAccountUser(
      @RequestBody @Validated ModifyAccountUserRequestDTO accountUserModifyRequestDTO
      , @AuthenticationPrincipal TokenAccountUserInfo accountUserInfo
      , BindingResult result
  ) {

      List<FieldError> validatedResult = FieldErrorResponse.getValidatedResult(result);
      if (validatedResult != null) {  // valid 검증을 했을때 에러가 있는지 확인
        return ResponseEntity.badRequest().body(validatedResult);
      }
      ModifyAccountUserResponseDTO accountUserModifyResponseDTO
          = accountUserService.accountUserModify(accountUserModifyRequestDTO, accountUserInfo);

      return ResponseEntity.ok().body(accountUserModifyResponseDTO);

  }

  // 회원 정보 삭제
  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteAccountUser(
      @RequestBody @Validated DeleteAccountUserRequestDTO accountUserDeleteRequestDTO
      , @AuthenticationPrincipal TokenAccountUserInfo accountUserInfo
      , BindingResult result
      ) {

      List<FieldError> validatedResult = FieldErrorResponse.getValidatedResult(result);
      if (validatedResult != null) {  // valid 검증을 했을때 에러가 있는지 확인
        return ResponseEntity.badRequest().body(validatedResult);
      }
    boolean value = accountUserService.accountUserDelete(accountUserDeleteRequestDTO, accountUserInfo);
      return ResponseEntity.ok().body(value);

  }
}


