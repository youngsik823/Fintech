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
public class DepositTransactionResponseDTO {

  private TransactionType transactionType;

  private Long transactionAmount;

  private String remittanceName;

  private String receivedName;

  private LocalDateTime transactedAt;

  private String remittanceAccount;

  public static DepositTransactionResponseDTO toEntity(Transaction transaction) {
    return DepositTransactionResponseDTO.builder()
        .transactionType(TransactionType.DEPOSIT)
        .transactionAmount(transaction.getTransactionAmount())
        .remittanceName(transaction.getRemittanceName())
        .receivedName(transaction.getReceivedName())
        .transactedAt(transaction.getTransactedAt())
        .remittanceAccount(transaction.getRemittanceAccount())
        .build();
  }
}
