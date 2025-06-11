package org.du2du.ensinaplus.model.dto.list;

import java.util.UUID;

import org.du2du.ensinaplus.model.entity.impl.ModuleResource;
import org.du2du.ensinaplus.model.enums.TypeResourceEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourceListDTO {
    private UUID uuid;
    private String file;
    private String video;
    private String name;
    private String descriptionHTML;
    private TypeResourceEnum type;

    public ResourceListDTO(ModuleResource moduleResource){
        this.uuid = moduleResource.getUuid();
        this.file = moduleResource.getFile();
        this.video = moduleResource.getVideo();
        this.name = moduleResource.getName();
        this.descriptionHTML = moduleResource.getDescriptionHTML();
        this.type = moduleResource.getType();
    }
}
