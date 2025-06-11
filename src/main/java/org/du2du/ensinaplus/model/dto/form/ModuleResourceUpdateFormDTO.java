package org.du2du.ensinaplus.model.dto.form;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModuleResourceUpdateFormDTO {
  @NotNull(message = "UUID é obrigatório")
  private UUID uuid;
  @NotBlank(message = "Nome é obrigatório")
  private String name;
  private String file;
  private String video;
  private String descriptionHTML;
}
