package com.ys.Fintech.transaction.api;

import com.ys.Fintech.exception.FieldErrorResponse;
import com.ys.Fintech.security.TokenAccountUserInfo;
import com.ys.Fintech.transaction.dto.request.RemittanceTransactionRequestDTO;
import com.ys.Fintech.transaction.dto.response.RemittanceTransactionResponseDTO;
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

  // 송금
  @PostMapping("/make")
  public ResponseEntity<?> makeTransaction(
      @RequestBody @Validated RemittanceTransactionRequestDTO receiveTransactionRequestDTO,
      @AuthenticationPrincipal TokenAccountUserInfo accountUserInfo,
      BindingResult result
      ) {
    List<FieldError> validatedResult = FieldErrorResponse.getValidatedResult(result);
    if (validatedResult != null) {
      return ResponseEntity.badRequest().body(validatedResult);
    }

    RemittanceTransactionResponseDTO value = transactionService.remittanceTransaction(receiveTransactionRequestDTO, accountUserInfo);
    return ResponseEntity.ok().body(value);

  }
}
