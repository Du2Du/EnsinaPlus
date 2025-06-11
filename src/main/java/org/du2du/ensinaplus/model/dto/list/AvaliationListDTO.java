package org.du2du.ensinaplus.model.dto.list;


import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class AvaliationListDTO {

    private String name;
    private String description;
    private Integer stars;

    public AvaliationListDTO(String name, String description, Integer stars){
        this.name = name;
        this.description = description;
        this.stars = stars;
    }

}
