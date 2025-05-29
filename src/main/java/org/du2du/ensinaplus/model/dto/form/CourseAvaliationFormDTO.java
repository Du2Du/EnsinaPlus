package org.du2du.ensinaplus.model.dto.form;

import java.util.UUID;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CourseAvaliationFormDTO {

  @NotNull()
  private UUID courseUUID;
  @NotNull()
  private Integer stars;
  @NotEmpty(message = "Comentário não informado")
  private String comment;
}
