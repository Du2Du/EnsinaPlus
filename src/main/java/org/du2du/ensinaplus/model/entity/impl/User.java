package org.du2du.ensinaplus.model.entity.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.du2du.ensinaplus.model.entity.AbstractEntity;
import org.du2du.ensinaplus.model.enums.RoleEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbuser")
@Getter
@Setter
public class User extends AbstractEntity {

  private String name;

  private String email;

  private String password;

  private String phone;
  private String picture;

  @Enumerated(EnumType.STRING)
  private RoleEnum role;

  public User() {
    super();
  }

  @Builder
  public User(UUID uuid, Boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt, String name, String email,
      String password, String phone, String picture, RoleEnum role) {
    super(uuid, deleted, createdAt, updatedAt);
    this.name = name;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.picture = picture;
    this.role = role;
  }

  public User(String name, String email, String password, String phone, String picture, RoleEnum role) {
    super();
    this.name = name;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.picture = picture;
    this.role = role;
  }
}
