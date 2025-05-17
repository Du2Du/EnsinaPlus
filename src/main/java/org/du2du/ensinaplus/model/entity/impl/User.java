package org.du2du.ensinaplus.model.entity.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.du2du.ensinaplus.model.dto.UserDTO;
import org.du2du.ensinaplus.model.entity.AbstractEntity;
import org.du2du.ensinaplus.model.enums.RoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
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

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private RoleEnum role;

  @OneToMany (mappedBy = "student")
  private List<CourseStudent> courses;

  public User() {
    super();
  }

  @Builder
  public User(UUID uuid, Boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt, String name, String email,
      String password, String phone, String picture, RoleEnum role, List<CourseStudent> courses) {
    super(uuid, deleted, createdAt, updatedAt);
    this.name = name;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.picture = picture;
    this.role = role;
    this.courses = courses;
  }

  public User(String name, String email, String password, String phone, String picture, RoleEnum role, List<CourseStudent> courses) {
    super();
    this.name = name;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.picture = picture;
    this.role = role;
    this.courses = courses;
  }

  public UserDTO toDTO() {
    return UserDTO.builder()
        .uuid(this.getUuid())
        .name(this.getName())
        .email(this.getEmail())
        .phone(this.getPhone())
        .picture(this.getPicture())
        .role(this.getRole())
        .build();
  }
}
