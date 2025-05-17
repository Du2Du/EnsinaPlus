package org.du2du.ensinaplus.model.entity.impl;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbcourse_student")
@IdClass(CourseStudentId.class)
@Getter
@Setter
public class CourseStudent {

    @Id
    @ManyToOne
    @JoinColumn(name ="student_uuid", referencedColumnName = "uuid")
    private User student;

    @Id
    @ManyToOne
    @JoinColumn(name ="course_uuid", referencedColumnName = "uuid")
    private Course course;

    @Column(name = "matriculation_date")
    private LocalDate matriculationDate;


}
