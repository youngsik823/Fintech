package com.ys.Fintech.account.dto.response;

import com.ys.Fintech.account.domain.Account;
import com.ys.Fintech.account.domain.AccountBank;
import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class GetAccountResponseDTO {

  private String name;

  private AccountBank accountBank;

  private String accountNum;

  private Long accountAmount;


  public GetAccountResponseDTO(Account account) {
    this.name = account.getAccountUser().getName();
    this.accountBank = account.getAccountBank();
    this.accountNum = account.getAccountNum();
    this.accountAmount = account.getAccountAmount();
  }
}
