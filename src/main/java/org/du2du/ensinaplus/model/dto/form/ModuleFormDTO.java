package org.du2du.ensinaplus.model.dto.form;

import java.util.UUID;
import org.du2du.ensinaplus.model.entity.impl.Module;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleFormDTO {
    private UUID uuid;
    @NotBlank( message = "Nome necessário")
    private String name;
    private String description;
    private Integer positionOrder;
    @NotNull(message = "Identificador de curso necessário")
    private UUID courseUuid;

    public Module toEntity(){
        return Module.builder().uuid(uuid).name(name).description(description).positionOrder(positionOrder).courseUuid(courseUuid).build();
    }
}
