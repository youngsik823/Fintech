package com.ys.Fintech.transaction.domain;

import com.ys.Fintech.BaseEntity;
import com.ys.Fintech.account.domain.Account;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"account"})
@Builder
@Entity
@DynamicInsert
public class Transaction extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "transaction_id")
  private Long transactionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id")
  private Account account;

  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type", nullable = false)  // 거래 타입
  private TransactionType transactionType;

  @Column(name = "transaction_amount", nullable = false)
  private Long transactionAmount; // 거래된 돈

  @Column(name = "remittance_name")
  private String remittanceName;  // 송금자 이름

  @Column(name = "remittance_account")
  private String remittanceAccount;  // 송금 계좌

  @Column(name = "received_name")
  private String receivedName;  // 받은사람 이름

  @Column(name = "transacted_at", nullable = false)
  private LocalDateTime transactedAt; // 거래 시간


}
