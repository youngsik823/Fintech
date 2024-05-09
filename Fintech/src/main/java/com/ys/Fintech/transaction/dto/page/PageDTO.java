package com.ys.Fintech.transaction.dto.page;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class PageDTO {

  private int page;
  private int size;

  public PageDTO() {
    this.page = 1;
    this.size = 10;
  }
}
