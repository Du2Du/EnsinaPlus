package org.du2du.ensinaplus.model.dto;

import java.util.List;
import java.util.UUID;

import org.du2du.ensinaplus.model.dto.list.ResourceListDTO;

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
public class ModuleDTO {
    private UUID uuid;
    private String name;
    private String description;
    private List<ResourceListDTO> resources;
    private Integer positionOrder;

    public ModuleDTO(UUID uuid, String name, String description, Integer positionOrder) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
    }
}
