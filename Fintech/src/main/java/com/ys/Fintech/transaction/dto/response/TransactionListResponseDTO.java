package com.ys.Fintech.transaction.dto.response;

import com.ys.Fintech.transaction.dto.page.PageResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionListResponseDTO {
  private int count;
  private PageResponseDTO pageInfo;
  private List<TransactionResponseDTO> transactions;
}
