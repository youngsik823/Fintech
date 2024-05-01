package com.ys.Fintech.security;

import com.ys.Fintech.accountUser.domain.Role;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenAccountUserInfo {

  private Long id;

  private String email;

  private Role role;

  private String phoneNum;

  private String name;
}
