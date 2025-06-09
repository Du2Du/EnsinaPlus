package org.du2du.ensinaplus.model.dto.base;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDTO<T> {
  private String title;
  private String description;
  private T data;
  private Long total;

  @Builder
  public ResponseDTO(String title, String description, T data, Long total){
    this.title = title;
    this.description = description;
    this.data = data;
    this.total= total;
  }
}
