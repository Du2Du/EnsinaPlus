package org.du2du.ensinaplus.model.dto.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResponseDTO<T> {
  private String title;
  private String description;
  private T data;
  private Long total;
}
