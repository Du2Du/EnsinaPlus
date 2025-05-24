package org.du2du.ensinaplus.model.entity.impl;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbcourse_student")
@Getter
@Setter
public class CourseStudent {

    @Id
    @EmbeddedId
    private CourseStudentId id;

    @ManyToOne
    @JoinColumn(name ="student_uuid", referencedColumnName = "uuid")
    private User student;

  
    @ManyToOne
    @JoinColumn(name ="course_uuid", referencedColumnName = "uuid")
    private Course course;

    @Column(name = "matriculation_date")
    private LocalDate matriculationDate;

    public CourseStudent(){

    }

    @Builder
    CourseStudent (CourseStudentId id, User student, Course course, LocalDate matriculationDate){
        this.id = id;
        this.student = student;
        this.course = course;
        this.matriculationDate = matriculationDate;
    }
}
