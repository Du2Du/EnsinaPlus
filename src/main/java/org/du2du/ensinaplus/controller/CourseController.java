package org.du2du.ensinaplus.controller;

import org.du2du.ensinaplus.model.bo.impl.CourseBO;
import org.du2du.ensinaplus.model.bo.impl.CourseStudentBO;
import org.du2du.ensinaplus.model.dto.CourseStudentDTO;
import org.du2du.ensinaplus.model.dto.form.CourseFormDTO;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.security.RequireRole;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("v1/course")
public class CourseController {

    @Inject
    CourseBO courseBO;

    @Inject
    CourseStudentBO courseStudentBO;

    @POST
    @RequireRole(RoleEnum.ROLE_TEACHER)
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCourse(CourseFormDTO course, @Context HttpHeaders headers) {
        return courseBO.createCourse(course, headers);
    }

    @POST
    @Path("enroll")
    @RequireRole(RoleEnum.ROLE_STUDENT)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response matriculateCourse(CourseStudentDTO courseStudentDTO, @Context HttpHeaders headers) {
        return courseStudentBO.matriculateUser(courseStudentDTO, headers);
    }

    @GET
    @Path("list")
    @RequireRole(RoleEnum.ROLE_STUDENT)
    public Response listAllCourses() {
        return courseBO.listAllCourses();
    }

    @GET
    @Path("enrollment")
    @RequireRole(RoleEnum.ROLE_STUDENT)
    public Response listEnrollmentCourses(@Context HttpHeaders headers) {
        return courseBO.listEnrollmentCourses(headers);
    }

    @GET
    @Path("list/created")
    @RequireRole(RoleEnum.ROLE_TEACHER)
    public Response listCreatedCourses(@Context HttpHeaders headers) {
        return courseBO.listCreatedCourses(headers);
    }
}
