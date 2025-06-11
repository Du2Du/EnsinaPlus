package org.du2du.ensinaplus.model.dto.form;

import java.util.UUID;

import org.du2du.ensinaplus.model.enums.UserTypeEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserUpdateFormDTO {
  @NotNull(message = "UUID é obrigatório")
  private UUID uuid;
  @NotBlank(message = "Nome é obrigatório")
  private String name;
  @NotBlank(message = "Email é obrigatório")
  @Email(message = "Email inválido")
  private String email;
  private String phone;
  private UserTypeEnum type;
}
