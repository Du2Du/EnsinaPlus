package org.du2du.ensinaplus.model.dto.form;

import org.du2du.ensinaplus.model.entity.impl.User;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.utils.PasswordUtils;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserFormDTO {
  @NotBlank(message = "Nome é obrigatório")
  private String name;
  @NotBlank(message = "Email é obrigatório")
  @Email(message = "Email inválido")
  private String email;
  @NotBlank(message = "Senha é obrigatório")
  @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
  private String password;

  @NotBlank(message = "Tipo de usuário é obrigatório")
  private RoleEnum role;


  public User toEntity() {
    return User.builder()
        .name(name)
        .email(email)
        .role(role)
        .password(PasswordUtils.hashPassword(password))
        .build();
  }
}
