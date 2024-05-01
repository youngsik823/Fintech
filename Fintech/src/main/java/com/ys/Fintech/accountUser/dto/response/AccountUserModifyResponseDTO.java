package com.ys.Fintech.accountUser.dto.response;

import com.ys.Fintech.accountUser.domain.AccountUser;
import com.ys.Fintech.accountUser.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUserModifyResponseDTO {

  private String email;

  private String name;

  private String phoneNum;

  private Role role;

  public AccountUserModifyResponseDTO(AccountUser accountUser) {
    this.email = accountUser.getEmail();
    this.name = accountUser.getName();
    this.role = accountUser.getRole();
    this.phoneNum = accountUser.getPhoneNum();
  }
}
