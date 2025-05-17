package org.du2du.ensinaplus.model.dto.form;

import java.util.UUID;

import org.du2du.ensinaplus.model.entity.impl.Course;
import org.du2du.ensinaplus.model.entity.impl.User;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CourseFormDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String name;
    private String description;
    private String mainPicture;

    public Course toEntity(UUID uuidUser){
        return Course.builder()
        .name(name)
        .description(description)
        .mainPicture(mainPicture)
        .owner(User.builder().uuid(uuidUser).build())
        .build();
    }
}
