package org.du2du.ensinaplus.model.bo.impl;

import java.util.Objects;
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
import org.du2du.ensinaplus.utils.PasswordUtils;

import jakarta.enterprise.context.Dependent;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

@Dependent
public class UserBO extends AbstractBO<User, UserDAO> {

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
  public Response saveUser(UUID uuid, UserUpdateFormDTO dto, HttpHeaders headers) {
    ValidateDTO validateResp = validate(dto);
    if (!validateResp.isOk())
      return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();

    User userEntity = dao.findById(uuid);
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
      userDTO.setRole(sessionBO.getSession(headers).getData().getRole());
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
    if (!PasswordUtils.comparePassword(user.getPassword(), userEntity.getPassword()))
      return Response.status(Response.Status.NOT_FOUND)
          .entity(ResponseDTO.builder().title("Erro ao logar!")
              .description("Senha incorreta.").build())
          .build();
    UserDTO userDTO = userEntity.toDTO();
    userDTO.setRole(RoleEnum.valueOf(role.toUpperCase()));

    NewCookie sessionCookie = sessionBO.createSession(userDTO);
    NewCookie authCookie = sessionBO.createAuthCookie(role);

    return Response.ok(ResponseDTO.builder().title("Login realizado com sucesso!").data(userDTO).build())
        .cookie(sessionCookie, authCookie)
        .build();
  }

  public Response getUserDTO(@Context HttpHeaders headers) {
    var session = sessionBO.getSession(headers);
    return Response.ok().entity(Objects.isNull(session) ? null : session.getData()).build();
  }

}
