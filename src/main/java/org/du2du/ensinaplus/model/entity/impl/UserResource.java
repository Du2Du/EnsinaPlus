package org.du2du.ensinaplus.model.entity.impl;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbuser_resource", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "user_uuid", "resource_uuid" }) })
@Getter
@Setter
public class UserResource extends PanacheEntityBase {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;

  @ManyToOne
  @JoinColumn(name = "user_uuid", referencedColumnName = "uuid", columnDefinition = "uuid not null references tbuser(uuid)")
  private User user;
  @ManyToOne
  @JoinColumn(name = "resource_uuid", referencedColumnName = "uuid", columnDefinition = "uuid not null references tbmodule_resource(uuid)")
  private ModuleResource resource;

  public UserResource() {
  }

  @Builder
  public UserResource(User user, ModuleResource resource) {
    this.user = user;
    this.resource = resource;
  }
}
