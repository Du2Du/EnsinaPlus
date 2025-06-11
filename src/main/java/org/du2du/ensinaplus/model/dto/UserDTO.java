package org.du2du.ensinaplus.model.dto;

import java.util.UUID;

import org.du2du.ensinaplus.model.entity.impl.User;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.model.enums.UserTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String name;
    private String email;
    private RoleEnum role;
    private UserTypeEnum type;
    private UUID uuid;
    private String phone;

    public UserDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.type = user.getType();
        this.uuid = user.getUuid();
        this.phone = user.getPhone();
    }
}