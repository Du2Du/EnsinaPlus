package org.du2du.ensinaplus.controller;

import java.util.UUID;

import org.du2du.ensinaplus.model.bo.impl.UserBO;
import org.du2du.ensinaplus.model.dto.UserLoginDTO;
import org.du2du.ensinaplus.model.dto.form.UserFormDTO;
import org.du2du.ensinaplus.model.dto.form.UserUpdateFormDTO;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.security.ActionDescription;
import org.du2du.ensinaplus.security.NotRequiredAudit;
import org.du2du.ensinaplus.security.RequiredAuthentication;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("v1/user")
public class UserController {

  @Inject
  UserBO bo;

  @POST
  @Path("create")
  @NotRequiredAudit
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createUser(UserFormDTO user) {
    return bo.createUser(user);
  }

  @PUT
  @Path("save")
  @RequiredAuthentication()
  @ActionDescription("Atualizou os seus dados básicos")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response saveUser(UserUpdateFormDTO user, @Context HttpHeaders headers) {
    return bo.saveUser(user, headers);
  }

  @POST
  @Path("login/teacher")
  @NotRequiredAudit
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response loginTeacher(UserLoginDTO user) {
    return bo.login(user, RoleEnum.ROLE_TEACHER);
  }

  @POST
  @Path("login/admin")
  @NotRequiredAudit
  public Response loginAdmin(UserLoginDTO user) {
    return bo.login(user, RoleEnum.ROLE_ADMIN);
  }

  @POST
  @Path("login/student")
  @NotRequiredAudit
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response loginStudent(UserLoginDTO user) {
    return bo.login(user, RoleEnum.ROLE_STUDENT);
  }

  @GET
  @Path("dto")
  @Produces(MediaType.APPLICATION_JSON)
  @RequiredAuthentication()
  @ActionDescription("Buscou dto do usuário logado")
  public Response getUserDTO(@Context HttpHeaders headers) {
    return bo.getUserDTO(headers);
  }
}
