package org.du2du.ensinaplus.model.dto.form;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseAvaliationFormDTO {

  @NotNull(message = "Curso não informado")
  private UUID courseUUID;
  @NotNull(message = "Estrelas não informado")
  private Integer stars;
  @NotEmpty(message = "Comentário não informado")
  private String comment;
}
