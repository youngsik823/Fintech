package com.ys.Fintech.transaction.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class DepositTransactionRequestDTO {

  @NotBlank
  @Size(min = 4, max = 20)
  private String password;

  @NotNull
  private Long accountId;

  @NotNull
  @Min(value = 1, message = "최소 1원부터 입금 하실 수 있습니다.")
  @Max(value = 10000000, message = "최대 1000만원까지 입급 하실 수 있습니다.")
  private Long transactionAmount;

  @NotBlank
  private String remittanceAccount;


}
