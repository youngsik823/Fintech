package com.ys.Fintech.transaction.api;

import com.ys.Fintech.exception.FieldErrorResponse;
import com.ys.Fintech.security.TokenAccountUserInfo;
import com.ys.Fintech.transaction.dto.page.PageDTO;
import com.ys.Fintech.transaction.dto.request.DepositTransactionRequestDTO;
import com.ys.Fintech.transaction.dto.request.WithdrawTransactionRequestDTO;
import com.ys.Fintech.transaction.dto.response.DepositTransactionResponseDTO;
import com.ys.Fintech.transaction.dto.response.TransactionListResponseDTO;
import com.ys.Fintech.transaction.dto.response.WithdrawTransactionResponseDTO;
import com.ys.Fintech.transaction.service.TransactionService;
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
@RequestMapping("/api/v1/transaction")
public class TransactionController {
  private final TransactionService transactionService;

  // 입금
  @PostMapping("/deposit")
  public ResponseEntity<?> depositTransaction(
      @RequestBody @Validated DepositTransactionRequestDTO depositTransactionRequestDTO,
      BindingResult result,
      @AuthenticationPrincipal TokenAccountUserInfo accountUserInfo
      ) {
    List<FieldError> validatedResult = FieldErrorResponse.getValidatedResult(result);
    if (validatedResult != null) {
      return ResponseEntity.badRequest().body(validatedResult);
    }
    DepositTransactionResponseDTO value = transactionService.depositTransaction(depositTransactionRequestDTO, accountUserInfo);
    return ResponseEntity.ok().body(value);
  }

  // 출금
  @PostMapping("/withdraw")
  public ResponseEntity<?> withdrawTransaction(
      @RequestBody @Validated WithdrawTransactionRequestDTO withdrawTransactionRequestDTO,
      BindingResult result,
      @AuthenticationPrincipal TokenAccountUserInfo accountUserInfo
  ) {
    List<FieldError> validatedResult = FieldErrorResponse.getValidatedResult(result);
    if (validatedResult != null) {
      return ResponseEntity.badRequest().body(validatedResult);
    }
    WithdrawTransactionResponseDTO value = transactionService.withdrawTransaction(withdrawTransactionRequestDTO, accountUserInfo);
    return ResponseEntity.ok().body(value);
  }

  // 내역 조회
  @GetMapping("/myTransaction/{accountId}")
  public ResponseEntity<?> getTransaction(
      @AuthenticationPrincipal TokenAccountUserInfo accountUserInfo,
      @PathVariable("accountId") Long accountId,
      @RequestBody PageDTO pageDTO
  ) {
    log.info("pageDTO @@@@@@@@@@-- {} ", pageDTO);
    TransactionListResponseDTO value = transactionService.getTransaction(accountUserInfo, accountId, pageDTO);
    return ResponseEntity.ok().body(value);
  }
}
