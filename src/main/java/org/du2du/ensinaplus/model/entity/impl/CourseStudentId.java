package org.du2du.ensinaplus.model.entity.impl;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class CourseStudentId implements Serializable {
    @Column(name = "student_uuid")
    private UUID student;

    @Column(name = "course_uuid")
    private UUID course;

    @Builder
    public CourseStudentId(UUID student, UUID course){
        this.student = student;
        this.course = course;
    }
}
