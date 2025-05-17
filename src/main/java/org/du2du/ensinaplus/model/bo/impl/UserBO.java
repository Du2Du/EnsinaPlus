package org.du2du.ensinaplus.model.bo.impl;

import java.util.Objects;

import org.du2du.ensinaplus.model.bo.AbstractBO;
import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dao.impl.UserDAO;
import org.du2du.ensinaplus.model.dto.UserLoginDTO;
import org.du2du.ensinaplus.model.dto.base.ResponseDTO;
import org.du2du.ensinaplus.model.dto.base.ValidateDTO;
import org.du2du.ensinaplus.model.dto.form.UserFormDTO;
import org.du2du.ensinaplus.model.entity.impl.User;
import org.du2du.ensinaplus.utils.PasswordUtils;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Dependent
public class UserBO extends AbstractBO<User, UserDAO> {

  @Inject
  SessionBO sessionBO;

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

  public Response login(UserLoginDTO user, String role) {
    User userEntity = dao.findByEmailAndPassword(user.getEmail(), PasswordUtils.hashPassword(user.getPassword()));
    if (Objects.isNull(userEntity))
      return Response.status(Response.Status.NOT_FOUND)
          .entity(ResponseDTO.builder().title("Erro ao logar!")
              .description("Usuário não encontrado.").build())
          .build();

    sessionBO.createSession(userEntity.toDTO());
    return Response.ok(ResponseDTO.builder().title("Login realizado com sucesso!").build())
        .cookie(sessionBO.createAuthCookie(role))
        .build();
  }

}
