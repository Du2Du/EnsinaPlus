package org.du2du.ensinaplus.model.entity.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.du2du.ensinaplus.model.dto.UserDTO;
import org.du2du.ensinaplus.model.entity.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbuser")
@Getter
@Setter
public class User extends AbstractEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  private String phone;
  private String picture;

  public User() {
    super();
  }

  @Builder
  public User(UUID uuid, Boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt, String name, String email,
      String password, String phone, String picture) {
    super(uuid, deleted, createdAt, updatedAt);
    this.name = name;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.picture = picture;
  }

  public User(String name, String email, String password, String phone, String picture) {
    super();
    this.name = name;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.picture = picture;
  }

  public UserDTO toDTO() {
    return UserDTO.builder()
        .uuid(this.getUuid())
        .name(this.getName())
        .email(this.getEmail())
        .phone(this.getPhone())
        .picture(this.getPicture())
        .build();
  }
}
