package com.ys.Fintech.accountUser.dto.request;

import com.ys.Fintech.accountUser.domain.AccountUser;
import com.ys.Fintech.accountUser.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.slf4j.Slf4j;


@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Slf4j

public class SignUpRequestDTO {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 4, max = 20)
  private String password;

  @NotBlank
  @Size(min = 1, max = 10)
  private String name;

  @NotBlank
  @Size(min = 11, max = 11)
  private String phoneNum;

  private Role role;



  public AccountUser toEntity() {
    return AccountUser.builder()
        .email(this.email)
        .password(this.password)
        .name(this.name)
        .phoneNum(this.phoneNum)
        .role(this.role)
        .build();
  }
}
