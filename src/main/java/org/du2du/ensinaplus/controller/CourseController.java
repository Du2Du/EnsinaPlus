package org.du2du.ensinaplus.controller;

import org.du2du.ensinaplus.model.bo.impl.CourseBO;
import org.du2du.ensinaplus.model.bo.impl.CourseStudentBO;
import org.du2du.ensinaplus.model.dto.CourseStudentDTO;
import org.du2du.ensinaplus.model.dto.form.CourseFormDTO;
import org.du2du.ensinaplus.security.RequiredAuthentication;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("v1/course")
public class CourseController {

    @Inject
    CourseBO courseBO;

    @Inject
    CourseStudentBO courseStudentBO;

    @POST
    @RequiredAuthentication
    @Path("create")
    public Response createCourse (CourseFormDTO course){
        return courseBO.createCourse(course);
    }
    
    @POST
    @RequiredAuthentication
    @Path("enroll")
    public Response matriculateCourse(CourseStudentDTO courseStudentDTO){
        return courseStudentBO.matriculateUser(courseStudentDTO);
    }
}
