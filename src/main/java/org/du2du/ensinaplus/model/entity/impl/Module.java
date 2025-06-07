package org.du2du.ensinaplus.model.entity.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.du2du.ensinaplus.model.entity.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Module extends AbstractEntity{

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @OneToOne
    @JoinColumn(name="course_uuid", nullable = false)
    private Course course;

    @Builder
  public Module(UUID uuid, Boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt, String name, String description,
      UUID courseUuid) {
    super(uuid, deleted, createdAt, updatedAt);
    this.name = name;
    this.description = description;
    this.course = Course.builder().uuid(courseUuid).build();
  }
}
