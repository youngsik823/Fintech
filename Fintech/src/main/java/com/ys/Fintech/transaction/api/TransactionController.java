package com.ys.Fintech.transaction.api;

import com.ys.Fintech.exception.FieldErrorResponse;
import com.ys.Fintech.security.TokenAccountUserInfo;
import com.ys.Fintech.transaction.dto.request.DepositTransactionRequestDTO;
import com.ys.Fintech.transaction.dto.response.DepositTransactionResponseDTO;
import com.ys.Fintech.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/v1/transaction")
public class TransactionController {
  private final TransactionService transactionService;

  // 입금
  @PostMapping("/remittance")
  public ResponseEntity<?> remittanceTransaction(
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

    // 입금
  }
}
