package com.ys.Fintech.accountUser.dto.response;

import com.ys.Fintech.accountUser.domain.AccountUser;
import com.ys.Fintech.accountUser.domain.Role;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SignInResponseDTO {
  private String email;
  private String name;
  private Role role;
  private String phoneNum;
  private String token; // 인증 토큰

  public SignInResponseDTO(AccountUser accountUser, String token) {
    this.email = accountUser.getEmail();
    this.name = accountUser.getName();
    this.role = accountUser. getRole();
    this.phoneNum = accountUser.getPhoneNum();
    this.token = token;
  }
}
