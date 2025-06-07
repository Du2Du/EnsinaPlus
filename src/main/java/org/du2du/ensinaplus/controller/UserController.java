package org.du2du.ensinaplus.controller;

import java.util.UUID;

import org.du2du.ensinaplus.model.bo.impl.UserBO;
import org.du2du.ensinaplus.model.dto.UserLoginDTO;
import org.du2du.ensinaplus.model.dto.form.UserFormDTO;
import org.du2du.ensinaplus.model.dto.form.UserUpdateFormDTO;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.security.NotRequiredAudit;
import org.du2du.ensinaplus.security.RequiredAuthentication;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Path("v1/user")
public class UserController {

  @Inject
  UserBO bo;

  @POST
  @Path("create")
  @NotRequiredAudit
  public Response createUser(UserFormDTO user) {
    return bo.createUser(user);
  }

  @PUT
  @Path("save/{uuid}")
  @RequiredAuthentication()
  public Response saveUser(@PathParam("uuid") UUID uuid, UserUpdateFormDTO user, @Context HttpHeaders headers) {
    return bo.saveUser(uuid, user, headers);
  }

  @POST
  @Path("login/teacher")
  @NotRequiredAudit
  public Response loginTeacher(UserLoginDTO user) {
    return bo.login(user, RoleEnum.ROLE_TEACHER);
  }

  @POST
  @Path("login/student")
  @NotRequiredAudit
  public Response loginStudent(UserLoginDTO user) {
    return bo.login(user, RoleEnum.ROLE_STUDENT);
  }

  @GET
  @Path("dto")
  @RequiredAuthentication()
  public Response getUserDTO(@Context HttpHeaders headers) {
    return bo.getUserDTO(headers);
  }
}
