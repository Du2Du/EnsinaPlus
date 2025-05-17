package org.du2du.ensinaplus.model.entity.impl;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CourseStudentId implements Serializable {
    @Column(name = "student_uuid")
    private UUID student;

    @Column(name = "course_uuid")
    private UUID course;
}
