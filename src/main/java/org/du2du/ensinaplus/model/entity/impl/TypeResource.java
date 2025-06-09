package org.du2du.ensinaplus.model.entity.impl;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbtype_resource")
@Getter
@Setter
@NoArgsConstructor
public class TypeResource extends PanacheEntityBase {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;

  @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
  private Boolean deleted;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "tag", nullable = false)
  private String tag;

  @Builder
  public TypeResource(UUID uuid, Boolean deleted, String name, String tag) {
    this.uuid = uuid;
    this.deleted = deleted;
    this.name = name;
    this.tag = tag;
  }
}
