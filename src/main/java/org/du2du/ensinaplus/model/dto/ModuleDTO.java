package org.du2du.ensinaplus.model.dto;

import java.util.UUID;

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
    private Integer positionOrder;
}
