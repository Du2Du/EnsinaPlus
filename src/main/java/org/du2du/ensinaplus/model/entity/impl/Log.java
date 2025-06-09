package org.du2du.ensinaplus.model.entity.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.du2du.ensinaplus.model.dto.LogDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tblog")
@Getter
@Setter
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name="method", nullable = false)
    private String method;

    @Column(name="url", nullable = false)
    private String url;

    @Column(name="uuid_user", nullable = false)
    private UUID uuidUser;

    @Column(name="name_user", nullable = false)
    private String nameUser;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Log(UUID uuid, String method, String url, UUID uuidUser, String nameUser, String description, LocalDateTime createdAt) {
        this.uuid = uuid;
        this.method = method;
        this.url = url;
        this.uuidUser = uuidUser;
        this.nameUser = nameUser;
        this.description = description;
        this.createdAt = createdAt;
    }
    
    public Log( String method, String url, UUID uuidUser,String nameUser, String description, LocalDateTime createdAt) {
        this.method = method;
        this.url = url;
        this.uuidUser = uuidUser;
        this.nameUser = nameUser;
        this.description = description;
        this.createdAt = createdAt;
    }
    public Log() {
    }

    public LogDTO toDTO() {
    return LogDTO.builder()
        .uuid(this.getUuid())
        .method(this.getMethod())
        .url(this.getUrl())
        .uuidUser(this.getUuidUser())
        .description(this.getDescription())
        .nameUser(this.getNameUser())
        .createdAt(this.getCreatedAt())
        .build();
  }

}
