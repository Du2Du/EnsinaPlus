package org.du2du.ensinaplus.rest;

import org.du2du.ensinaplus.model.bo.impl.UserBO;
import org.du2du.ensinaplus.model.dto.UserLoginDTO;
import org.du2du.ensinaplus.model.dto.form.UserFormDTO;
import org.du2du.ensinaplus.model.enums.RoleEnum;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("v1/user")
public class UserRest {

  @Inject
  UserBO bo;

  @POST
  @Path("create/teacher")
  @PermitAll
  public Response createTeacherUser(UserFormDTO user) {
    user.setRole(RoleEnum.TEACHER);
    return bo.createUser(user);
  }

  @POST
  @Path("create/student")
  @PermitAll
  public Response createStudentUser(UserFormDTO user) {
    user.setRole(RoleEnum.STUDENT);
    return bo.createUser(user);
  }

  @POST
  @Path("login/teacher")
  @PermitAll
  public Response loginTeacher(UserLoginDTO user) {
    return bo.login(user, RoleEnum.ROLE_TEACHER);
  }

  @POST
  @Path("login/student")
  @PermitAll
  public Response loginStudent(UserLoginDTO user) {
    return bo.login(user, RoleEnum.ROLE_STUDENT);
  }
}
