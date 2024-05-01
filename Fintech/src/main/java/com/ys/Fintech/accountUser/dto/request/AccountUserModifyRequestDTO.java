package com.ys.Fintech.accountUser.dto.request;

import com.ys.Fintech.accountUser.domain.AccountUser;
import com.ys.Fintech.accountUser.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUserModifyRequestDTO {

  @NotBlank
  @Size(min = 1, max = 10)
  private String name;

  @NotBlank
  @Size(min = 11, max = 11)
  private String phoneNum;

  private Role role;

  public AccountUser modifyAccountUser(AccountUser accountUser, AccountUserModifyRequestDTO accountUserModifyRequestDTO) {
    return AccountUser.builder()
        .id(accountUser.getId())
        .email(accountUser.getEmail())
        .password(accountUser.getPassword())
        .phoneNum(accountUserModifyRequestDTO.getPhoneNum())
        .role(accountUserModifyRequestDTO.getRole())
        .name(accountUserModifyRequestDTO.getName())
        .build();
  }
}
