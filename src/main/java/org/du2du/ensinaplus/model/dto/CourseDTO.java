package org.du2du.ensinaplus.model.dto;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import org.du2du.ensinaplus.model.entity.impl.Course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDTO {
    private String name;
    private String description;
    private String mainPicture;
    private UUID uuid;
    private UserDTO owner;
    private boolean matriculado;
    private Float avaliationAvg;

    public CourseDTO(Course course, LocalDate matriculationDate) {
        this.name = course.getName();
        this.matriculado = Objects.nonNull(matriculationDate);
        this.description = course.getDescription();
        this.mainPicture = course.getMainPicture();
        this.uuid = course.getUuid();
    }
}