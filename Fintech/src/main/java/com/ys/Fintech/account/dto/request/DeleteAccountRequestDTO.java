package com.ys.Fintech.account.dto.request;

import com.ys.Fintech.accountUser.domain.AccountUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class DeleteAccountRequestDTO {
  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 4, max = 20)
  private String password;

  @NotNull
  private Long accountId;

}
