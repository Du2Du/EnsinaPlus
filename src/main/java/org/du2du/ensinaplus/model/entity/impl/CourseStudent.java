package org.du2du.ensinaplus.model.entity.impl;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbcourse_student")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseStudent {

    @EmbeddedId
    CourseStudentId id;

    @ManyToOne
    @MapsId("student")
    @JoinColumn(name = "student_uuid")
    private User student;

    @ManyToOne
    @MapsId("course")
    @JoinColumn(name = "course_uuid")
    private Course course;

    @Column(name = "matriculation_date")
    private LocalDate matriculationDate;

    @Column(name = "conclusion_date")
    private LocalDate conclusionDate;

    @Column(name = "avaliation", columnDefinition = "text")
    private String avaliation;

    private Integer stars;
}
