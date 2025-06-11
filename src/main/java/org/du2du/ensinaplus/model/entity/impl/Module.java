package org.du2du.ensinaplus.model.entity.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.du2du.ensinaplus.model.entity.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbmodule")
@Getter
@Setter
@Table(name = "tbmodule")
@NoArgsConstructor
public class Module extends AbstractEntity{

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="position_order")
    private Integer positionOrder;

    @ManyToOne
    @JoinColumn(name="course_uuid", nullable = false)
    private Course course;

    @Builder
  public Module(UUID uuid, Boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt, String name, String description,
    Integer positionOrder, UUID courseUuid) {
    super(uuid, deleted, createdAt, updatedAt);
    this.name = name;
    this.description = description;
    this.positionOrder = positionOrder;
    this.course = Course.builder().uuid(courseUuid).build();
  }
}
