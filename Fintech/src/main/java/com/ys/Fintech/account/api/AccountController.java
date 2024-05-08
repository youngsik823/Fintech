package com.ys.Fintech.account.api;

import com.ys.Fintech.account.dto.response.CreateAccountResponseDTO;
import com.ys.Fintech.account.service.AccountService;
import com.ys.Fintech.security.TokenAccountUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
  private final AccountService accountService;

  // 계정 생성
  @PostMapping("/create")
  public ResponseEntity<?> createAccount(
      @AuthenticationPrincipal TokenAccountUserInfo accountUserInfo
  ) {
    CreateAccountResponseDTO account = accountService.createAccount(accountUserInfo);
    return ResponseEntity.ok().body(account);
  }

}
