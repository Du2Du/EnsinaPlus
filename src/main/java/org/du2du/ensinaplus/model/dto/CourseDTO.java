package org.du2du.ensinaplus.model.dto;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDTO{
    private String name;
    private String description;
    private String mainPicture;
    private UUID uuid;
    private UserDTO owner;
}