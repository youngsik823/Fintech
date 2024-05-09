package com.ys.Fintech.transaction.dto.response;

import com.ys.Fintech.account.domain.Account;
import com.ys.Fintech.account.domain.AccountBank;
import com.ys.Fintech.transaction.domain.Transaction;
import com.ys.Fintech.transaction.domain.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class TransactionResponseDTO {

  private Long transactionId;

  private String name;  // 회원이름

  private AccountBank accountBank;  // 은행

  private Long accountAmount; // 현재 잔액

  private TransactionType transactionType;  // 입금, 출금

  private Long transactionAmount; // 거래된 돈

  private String depositWithdrawName;  // 입출금자 이름

  private String depositWithdrawAccount;  // 입출금 계좌

  private String receivedName;  // 받은사람 이름

  private LocalDateTime transactedAt; // 거래 시간

  public TransactionResponseDTO(Transaction transaction) {
    this.transactionId = transaction.getTransactionId();
    this.name = transaction.getAccount().getAccountUser().getName();
    this.accountBank = transaction.getAccount().getAccountBank();
    this.accountAmount = transaction.getAccount().getAccountAmount();
    this.transactionType = transaction.getTransactionType();
    this.transactionAmount = transaction.getTransactionAmount();
    this.depositWithdrawName = transaction.getDepositWithdrawName();
    this.depositWithdrawAccount = transaction.getDepositWithdrawAccount();
    this.receivedName = transaction.getReceivedName();
    this.transactedAt = transaction.getTransactedAt();
  }
}
