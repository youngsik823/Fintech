package com.ys.Fintech.account.domain;

import com.ys.Fintech.BaseEntity;
import com.ys.Fintech.accountUser.domain.AccountUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"accountUser"})
@Builder
@Entity
@DynamicInsert
public class Account extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "account_id")
  private Long accountId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_user_id")
  private AccountUser accountUser;

  @Column(name = "account_bank", nullable = false)
  @Enumerated(EnumType.STRING)
  @ColumnDefault("'APPLE'")
  private  AccountBank accountBank;

  @Column(name = "account_num", nullable = false)
  private String accountNum;

  @Column(name = "account_amount", nullable = false)
  @ColumnDefault("0")
  private Long accountAmount;

  @Column(name = "account_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private AccountStatus accountStatus;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

}
