package com.ys.Fintech.accountUser.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.Fintech.accountUser.domain.AccountUser;
import com.ys.Fintech.accountUser.domain.Role;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@ToString
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class SignUpResponseDTO {
  private String email;
  private String name;
  private Role role;

  @JsonFormat(pattern = "yyyy년 MM월 dd일 HH시 mm분 ss초")
  private LocalDateTime createdAt;

  public SignUpResponseDTO(AccountUser accountUser) {
    this.email = accountUser.getEmail();
    this.name = accountUser.getName();
    this.role = accountUser.getRole();
    this.createdAt = accountUser.getCreatedAt();
  }
}
