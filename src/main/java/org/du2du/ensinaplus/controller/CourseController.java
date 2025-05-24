package org.du2du.ensinaplus.controller;

import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.security.RequireRole;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("v1/course")
public class CourseController {
    
    @GET
    @Path("enrollment")
    @RequireRole(RoleEnum.ROLE_STUDENT)
    public Response getStudentCourses() {
        return Response.ok().build();
    }
}
