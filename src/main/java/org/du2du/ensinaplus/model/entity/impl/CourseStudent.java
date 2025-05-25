package org.du2du.ensinaplus.model.entity.impl;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbcourse_student")
@Getter
@Setter
public class CourseStudent {

    @EmbeddedId
    CourseStudentId id;
    
    @ManyToOne
    @MapsId("student")
    @JoinColumn(name ="student_uuid")
    private User student;

    
    @ManyToOne
    @MapsId("course")
    @JoinColumn(name ="course_uuid")
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
