package org.du2du.ensinaplus.model.entity.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.du2du.ensinaplus.model.entity.AbstractEntity;
import org.du2du.ensinaplus.model.enums.TypeResourceEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbmodule_resource")
@Getter
@Setter
@NoArgsConstructor
public class ModuleResource extends AbstractEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description_html", columnDefinition = "text")
  private String descriptionHTML;

  @Column(name = "file", columnDefinition = "text")
  private String file;

  @Column(name = "video", columnDefinition = "text")
  private String video;

  @ManyToOne
  @JoinColumn(name = "module_uuid", referencedColumnName = "uuid", columnDefinition = "uuid not null references tbmodule(uuid)")
  private Module module;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private TypeResourceEnum type;

  @Builder
  public ModuleResource(UUID uuid, Boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt, String name,
      String descriptionHTML, Module module, TypeResourceEnum type, String file, String video) {
    super(uuid, deleted, createdAt, updatedAt);
    this.name = name;
    this.descriptionHTML = descriptionHTML;
    this.module = module;
    this.type = type;
    this.file = file;
    this.video = video;
  }

}
