package com.ys.Fintech.account.dto.response;

import com.ys.Fintech.account.domain.Account;
import com.ys.Fintech.account.domain.AccountBank;
import com.ys.Fintech.account.domain.AccountStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class CreateAccountResponseDTO {
  private String name;
  private AccountBank accountBank;
  private String accountNum;
  private Long accountAmount;
  private AccountStatus accountStatus;
  private LocalDateTime createAt;

  public static CreateAccountResponseDTO toEntity(Account account) {
    return CreateAccountResponseDTO.builder()
        .name(account.getAccountUser().getName())
        .accountBank(account.getAccountBank())
        .accountNum(account.getAccountNum())
        .accountAmount(account.getAccountAmount())
        .accountStatus(account.getAccountStatus())
        .createAt(account.getCreatedAt())
        .build();
  }
}
