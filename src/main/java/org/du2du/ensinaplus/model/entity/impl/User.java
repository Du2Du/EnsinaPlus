package org.du2du.ensinaplus.model.entity.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.du2du.ensinaplus.model.dto.UserDTO;
import org.du2du.ensinaplus.model.entity.AbstractEntity;
import org.du2du.ensinaplus.model.enums.UserTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
  @Column(name = "picture", columnDefinition = "text")
  private String picture;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private UserTypeEnum type;

  @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
  private List<CourseStudent> courses;

  public User() {
    super();
  }

  @Builder
  public User(UUID uuid, Boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt, String name, String email,
      String password, String phone, String picture, UserTypeEnum type) {
    super(uuid, deleted, createdAt, updatedAt);
    this.name = name;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.picture = picture;
    this.type = type;
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
        .type(this.getType())
        .phone(this.getPhone())
        .picture(this.getPicture())
        .build();
  }
}
