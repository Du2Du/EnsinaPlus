package org.du2du.ensinaplus.controller;

import org.du2du.ensinaplus.model.bo.impl.UserBO;
import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dto.UserLoginDTO;
import org.du2du.ensinaplus.model.dto.form.UserFormDTO;
import org.du2du.ensinaplus.model.dto.form.UserUpdateFormDTO;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.security.ActionDescription;
import org.du2du.ensinaplus.security.NotRequiredAudit;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("v1/user")
public class UserController {

  @Inject
  UserBO bo;

  @Inject
  SessionBO sessionBO;

  @POST
  @Path("create")
  @NotRequiredAudit
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createUser(UserFormDTO user) {
    return bo.createUser(user);
  }

  @PUT
  @Path("update")
  @Authenticated
  @ActionDescription("Atualizou os seus dados básicos")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response saveUser(UserUpdateFormDTO user) {
    return bo.saveUser(user);
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
  @Authenticated
  @ActionDescription("Buscou dto do usuário logado")
  public Response getUserDTO() {
    return Response.ok().entity(sessionBO.getUserDTO()).build();
  }

  @GET
  @Path("list/role")
  @ActionDescription("Listou os usuários")
  @RolesAllowed(RoleEnum.ROLE_SUPER_ADMIN)
  public Response searchCourses(@QueryParam("page") Integer page, @QueryParam("limit") Integer limit) {
    return bo.listUsers(page, limit);
  }

}
