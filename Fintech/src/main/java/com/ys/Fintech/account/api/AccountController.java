package com.ys.Fintech.account.api;

import com.ys.Fintech.account.dto.request.DeleteAccountRequestDTO;
import com.ys.Fintech.account.dto.response.CreateAccountResponseDTO;
import com.ys.Fintech.account.dto.response.GetAccountListResponseDTO;
import com.ys.Fintech.account.service.AccountService;
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
@RequestMapping("/api/v1/account")
public class AccountController {
  private final AccountService accountService;

  // 계좌 생성
  @PostMapping("/create")
  public ResponseEntity<?> createAccount(
      @AuthenticationPrincipal TokenAccountUserInfo accountUserInfo
  ) {
    CreateAccountResponseDTO account = accountService.createAccount(accountUserInfo);
    return ResponseEntity.ok().body(account);
  }

  // 계좌 삭제
  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteAccount(
      @RequestBody @Validated DeleteAccountRequestDTO deleteAccountRequestDTO,
      BindingResult result,
      @AuthenticationPrincipal TokenAccountUserInfo accountUserInfo
  ) {
    List<FieldError> validatedResult = FieldErrorResponse.getValidatedResult(result);
    if (validatedResult != null) {
      return ResponseEntity.badRequest().body(validatedResult);
    }

    boolean value = accountService.deleteAccount(deleteAccountRequestDTO, accountUserInfo);
    return ResponseEntity.ok().body(value);
  }

  // 계좌 조회
  @GetMapping("/myAccount")
  public ResponseEntity<?> getAccount(
      @AuthenticationPrincipal TokenAccountUserInfo accountUserInfo
  ) {
    GetAccountListResponseDTO account = accountService.getAccount(accountUserInfo);
    return ResponseEntity.ok().body(account);
  }


}
