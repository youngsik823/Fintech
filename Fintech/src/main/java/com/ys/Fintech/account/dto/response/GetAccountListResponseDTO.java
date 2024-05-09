package com.ys.Fintech.account.dto.response;
import lombok.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class GetAccountListResponseDTO {
  private List<GetAccountResponseDTO> accountList;
}
