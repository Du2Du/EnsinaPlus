package org.du2du.ensinaplus.model.dto.form;

import java.util.UUID;
import org.du2du.ensinaplus.model.entity.impl.Module;

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
    private String name;
    private String description;
    private UUID courseUuid;

    public Module toEntity(){
        return Module.builder().uuid(uuid).name(name).description(description).courseUuid(courseUuid).build();
    }
}
