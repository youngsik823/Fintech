package com.ys.Fintech.accountUser.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUserDeleteRequestDTO {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 4, max = 20)
  private String password;
}
