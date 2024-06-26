package com.ys.Fintech.accountUser.dto.response;

import com.ys.Fintech.accountUser.domain.AccountUser;
import com.ys.Fintech.accountUser.domain.Role;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyAccountUserResponseDTO {

  private String email;

  private String name;

  private String phoneNum;

  private Role role;

  public ModifyAccountUserResponseDTO(AccountUser accountUser) {
    this.email = accountUser.getEmail();
    this.name = accountUser.getName();
    this.role = accountUser.getRole();
    this.phoneNum = accountUser.getPhoneNum();
  }
}
