package com.ys.Fintech.transaction.dto.response;

import com.ys.Fintech.transaction.domain.Transaction;
import com.ys.Fintech.transaction.domain.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class WithdrawTransactionResponseDTO {

  private TransactionType transactionType;

  private Long transactionAmount;

  private String depositWithdrawName;

  private String receivedName;

  private LocalDateTime transactedAt;

  private String depositWithdrawAccount;

  public static WithdrawTransactionResponseDTO toEntity(Transaction transaction) {
    return WithdrawTransactionResponseDTO.builder()
        .transactionType(TransactionType.WITHDRAW)
        .transactionAmount(transaction.getTransactionAmount())
        .depositWithdrawName(transaction.getDepositWithdrawAccount())
        .receivedName(transaction.getReceivedName())
        .transactedAt(transaction.getTransactedAt())
        .depositWithdrawAccount(transaction.getDepositWithdrawAccount())
        .build();
  }
}
