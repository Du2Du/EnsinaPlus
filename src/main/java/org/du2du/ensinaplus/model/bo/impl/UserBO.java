package org.du2du.ensinaplus.model.bo.impl;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.du2du.ensinaplus.model.bo.AbstractBO;
import org.du2du.ensinaplus.model.dao.impl.UserDAO;
import org.du2du.ensinaplus.model.dto.UserDTO;
import org.du2du.ensinaplus.model.dto.UserLoginDTO;
import org.du2du.ensinaplus.model.dto.base.ResponseDTO;
import org.du2du.ensinaplus.model.dto.base.ValidateDTO;
import org.du2du.ensinaplus.model.dto.form.UserFormDTO;
import org.du2du.ensinaplus.model.dto.form.UserUpdateFormDTO;
import org.du2du.ensinaplus.model.entity.impl.User;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.model.enums.UserTypeEnum;
import org.du2du.ensinaplus.utils.PasswordUtils;
import org.du2du.ensinaplus.utils.TokenUtils;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

@Dependent
public class UserBO extends AbstractBO<User, UserDAO> {
    
  @Inject
  TokenUtils tokenUtils;
  
  private static final String SESSION_COOKIE_NAME = "ensina-plus-session";

  @Transactional
  public Response createUser(UserFormDTO user) {
    ValidateDTO validateResp = validate(user);
    if (!validateResp.isOk())
      return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();

    User userEntity = dao.findByEmail(user.getEmail());
    if (Objects.nonNull(userEntity))
      return Response.status(Response.Status.CONFLICT)
          .entity(ResponseDTO.builder().title("Erro ao criar usuário!")
              .description("Usuário já cadastrado.").build())
          .build();

    userEntity = user.toEntity();
    try {
      dao.persistAndFlush(userEntity);
      user.setPassword(null);
      return Response.status(Response.Status.CREATED)
          .entity(ResponseDTO.builder().title("Usuário criado com sucesso!").data(user).build())
          .build();
    } catch (Exception e) {
      e.printStackTrace();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(ResponseDTO.builder().title("Erro ao criar usuário!")
              .description(e.getMessage()).build())
          .build();
    }
  }

  @Transactional
  public Response saveUser(UserUpdateFormDTO dto, HttpHeaders headers) {
    ValidateDTO validateResp = validate(dto);
    if (!validateResp.isOk())
      return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();

    User userEntity = dao.findById(dto.getUuid());
    if (Objects.isNull(userEntity))
      return Response.status(Response.Status.CONFLICT)
          .entity(ResponseDTO.builder().title("Erro ao salvar usuário!")
              .description("Usuário não encontrado.").build())
          .build();

    userEntity.setName(dto.getName());
    userEntity.setEmail(dto.getEmail());
    userEntity.setPicture(dto.getPicture());
    userEntity.setPhone(dto.getPhone());
    try {
      dao.persistAndFlush(userEntity);
      UserDTO userDTO = userEntity.toDTO();
      userDTO.setRole(sessionBO.getSession(headers.getCookies().get((SESSION_COOKIE_NAME))).getData().getRole());
      sessionBO.updateSession(userDTO, headers);
      return Response.status(Response.Status.CREATED)
          .entity(ResponseDTO.builder().title("Usuário salvo com sucesso!").data(dto).build())
          .build();
    } catch (Exception e) {
      e.printStackTrace();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(ResponseDTO.builder().title("Erro ao salvar usuário!")
              .description(e.getMessage()).build())
          .build();
    }

  }

  public Response login(UserLoginDTO user, String role) {
    User userEntity = dao.findByEmail(user.getEmail());
    if (Objects.isNull(userEntity))
      return Response.status(Response.Status.NOT_FOUND)
          .entity(ResponseDTO.builder().title("Erro ao logar!")
              .description("Usuário não encontrado.").build())
          .build();

    if (role.equals(RoleEnum.ROLE_ADMIN) && !UserTypeEnum.ADMIN.equals(userEntity.getType()))
      return Response.status(Response.Status.NOT_FOUND)
          .entity(ResponseDTO.builder().title("Erro ao logar!")
              .description("Usuário não é administrador.").build())
          .build();

    if (!PasswordUtils.comparePassword(user.getPassword(), userEntity.getPassword()))
      return Response.status(Response.Status.NOT_FOUND)
          .entity(ResponseDTO.builder().title("Erro ao logar!")
              .description("Senha incorreta.").build())
          .build();
    UserDTO userDTO = userEntity.toDTO();
    userDTO.setRole(RoleEnum.valueOf(role.toUpperCase()));

    NewCookie sessionCookie = sessionBO.createSession(userDTO);

    return Response.ok(ResponseDTO.builder().title("Login realizado com sucesso!").data(tokenUtils.generate(Set.of(role))).build())
        .cookie(sessionCookie)
        .build();
  }

  public Response getUserDTO(@Context HttpHeaders headers) {
    var session = sessionBO.getSession(headers.getCookies().get((SESSION_COOKIE_NAME)));
    return Response.ok().entity(Objects.isNull(session) ? null : session.getData()).build();
  }

}
