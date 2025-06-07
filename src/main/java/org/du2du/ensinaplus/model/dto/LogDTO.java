package org.du2du.ensinaplus.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.du2du.ensinaplus.model.entity.impl.Log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogDTO {
   
    private UUID uuid;

    private String method;

    private String url;

    private UUID uuidUser;

    private String description;

    private LocalDateTime createdAt;
    public LogDTO(Log log) {
        this.uuid = log.getUuid();
        this.method = log.getMethod();
        this.url = log.getUrl();
        this.uuidUser = log.getUuidUser();
        this.description = log.getDescription();
        this.createdAt = log.getCreatedAt();

    }
    public Log toEntity(){
        return Log.builder()
            .method(method)
            .url(url)
            .uuidUser(uuidUser)
            .description(description)
            .createdAt(createdAt)
            .build();
    }
}
