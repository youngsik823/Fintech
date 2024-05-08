package com.ys.Fintech.transaction.dto.request;

import com.ys.Fintech.transaction.domain.TransactionType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class RemittanceTransactionRequestDTO {

  @NotBlank
  @Size(min = 4, max = 20)
  private String password;

  @NotNull
  private Long accountId;

  @NotNull
  @Min(1)
  @Max(10000000)
  private Long transactionAmount;

  @NotBlank
  private String remittanceAccount;


}
