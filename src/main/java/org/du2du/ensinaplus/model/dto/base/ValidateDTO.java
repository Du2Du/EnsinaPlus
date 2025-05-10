package org.du2du.ensinaplus.model.dto.base;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ValidateDTO {
  private String title;
  private String description;
  private Boolean ok;

  public boolean isOk() {
    return Objects.nonNull(ok) && ok;
  }

  public static ValidateDTO ok() {
    return ValidateDTO.builder().ok(true).build();
  }

  public static ValidateDTO error(String title, String description) {
    return ValidateDTO.builder().ok(false).title(title).description(description).build();
  }
}
