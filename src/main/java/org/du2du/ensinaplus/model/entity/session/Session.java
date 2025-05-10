package org.du2du.ensinaplus.model.entity.session;

import java.time.LocalDateTime;
import java.util.UUID;

import org.du2du.ensinaplus.model.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Session {
    
    private UUID uuid;
    private LocalDateTime createdAt;
    private UserDTO data;
}
