package org.du2du.ensinaplus.model.entity.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.du2du.ensinaplus.model.dto.CourseDTO;
import org.du2du.ensinaplus.model.entity.AbstractEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbcourse")
@Getter
@Setter
public class Course extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "main_picture", nullable = true, columnDefinition = "text")
    private String mainPicture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "uuid", name = "owner_uuid", nullable = false, columnDefinition = "uuid references tbuser(uuid)")
    private User owner;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CourseStudent> students;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Module> modules;

    @Column(name = "avaliation_avg", nullable = true)
    private Float avaliationAvg;

    public Course() {
        super();
    }

    @Builder
    public Course(UUID uuid, Boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt, String name,
            String description,
            String mainPicture, User owner, List<CourseStudent> students, Float avaliationAvg) {
        super(uuid, deleted, createdAt, updatedAt);
        this.name = name;
        this.description = description;
        this.mainPicture = mainPicture;
        this.owner = owner;
        this.students = students;
        this.avaliationAvg = avaliationAvg;
    }

    public Course(String name, String description, String mainPicture, User owner, List<CourseStudent> students) {
        super();
        this.name = name;
        this.description = description;
        this.mainPicture = mainPicture;
        this.owner = owner;
        this.students = students;
    }

    public CourseDTO toDTO() {
        return CourseDTO.builder()
                .name(this.getName())
                .description(this.getDescription())
                .mainPicture(this.getMainPicture())
                .uuid(this.getUuid())
                .avaliationAvg(this.getAvaliationAvg())
                .owner(this.getOwner().toDTO())
                .build();
    }
    public CourseDTO toDTO(Boolean concluido, Boolean matriculado, Boolean avaliado) {
        return CourseDTO.builder()
                .name(this.getName())
                .description(this.getDescription())
                .mainPicture(this.getMainPicture())
                .uuid(this.getUuid())
                .avaliado(avaliado)
                .concluido(concluido)
                .matriculado(matriculado)
                .avaliationAvg(this.getAvaliationAvg())
                .owner(this.getOwner().toDTO())
                .build();
    }

}
