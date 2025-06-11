package org.du2du.ensinaplus.model.dto.form;

import java.util.UUID;

import org.du2du.ensinaplus.model.entity.impl.Module;
import org.du2du.ensinaplus.model.entity.impl.ModuleResource;
import org.du2du.ensinaplus.model.enums.TypeResourceEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModuleResourceFormDTO {

  @NotBlank(message = "Nome é obrigatório")
  private String name;
  private String descriptionHTML;
  @NotNull(message = "Modulo é obrigatório")
  private UUID moduleUUID;
  @NotNull(message = "Tipo é obrigatório")
  private TypeResourceEnum type;
  private String file;
  private String video;

  public ModuleResource toEntity(){
    return ModuleResource.builder()
      .name(name)
      .file(file)
      .video(video)
      .descriptionHTML(descriptionHTML)
      .module(Module.builder().uuid(moduleUUID).build())
      .type(type)
      .build();
  }
}
