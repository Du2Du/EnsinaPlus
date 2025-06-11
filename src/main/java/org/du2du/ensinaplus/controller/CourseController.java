package org.du2du.ensinaplus.controller;

import java.util.UUID;

import org.du2du.ensinaplus.model.bo.impl.CourseBO;
import org.du2du.ensinaplus.model.bo.impl.CourseStudentBO;
import org.du2du.ensinaplus.model.dto.CourseStudentDTO;
import org.du2du.ensinaplus.model.dto.form.CourseAvaliationFormDTO;
import org.du2du.ensinaplus.model.dto.form.CourseFormDTO;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.security.ActionDescription;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("v1/course")
public class CourseController {

    @Inject
    CourseBO courseBO;

    @Inject
    CourseStudentBO courseStudentBO;

    @POST
    @RolesAllowed(RoleEnum.ROLE_TEACHER)
    @ActionDescription("Criando um curso")
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCourse(CourseFormDTO course) {
        return courseBO.createCourse(course);
    }

    @POST
    @Path("enroll")
    @RolesAllowed(RoleEnum.ROLE_STUDENT)
    @ActionDescription("Matriculou-se em um curso")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response matriculateCourse(CourseStudentDTO courseStudentDTO) {
        return courseStudentBO.matriculateUser(courseStudentDTO);
    }

    @DELETE
    @Path("unenroll/{uuid}")
    @RolesAllowed(RoleEnum.ROLE_STUDENT)
    @ActionDescription("Desmatriculou-se do curso")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response desmatricularCurso(@PathParam("uuid") UUID uuid) {
        return courseStudentBO.desmatricularCurso(uuid);
    }

    @GET
    @Path("list")
    @RolesAllowed(RoleEnum.ROLE_STUDENT)
    @ActionDescription("Listou todos os cursos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllCourses() {
        return courseBO.listAllCourses();
    }

    @GET
    @Path("{uuid}")
    @Authenticated
    public Response find(@PathParam("uuid") UUID uuid) {
        return courseBO.findCourse(uuid);
    }

    @GET
    @Path("search")
    @RolesAllowed({ RoleEnum.ROLE_STUDENT, RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_SUPER_ADMIN })
    public Response searchCourses(@QueryParam("search") String search, @QueryParam("page") Integer page,
            @QueryParam("limit") Integer limit) {
        return courseBO.searchCourse(search, page, limit);
    }

    @GET
    @Path("enrollment")
    @RolesAllowed(RoleEnum.ROLE_STUDENT)
    @ActionDescription("Listou todos os cursos que está matriculado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listEnrollmentCourses() {
        return courseBO.listEnrollmentCourses();
    }

    @GET
    @Path("list/created")
    @RolesAllowed(RoleEnum.ROLE_TEACHER)
    @ActionDescription("Listou todos os cursos de sua autoria")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCreatedCourses() {
        return courseBO.listCreatedCourses();
    }

    @PUT
    @RolesAllowed(RoleEnum.ROLE_TEACHER)
    @ActionDescription("Atualizou os dados básicos do curso")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("update")
    public Response updateCourse(CourseFormDTO course) {
        return courseBO.updateCourse(course);
    }

    @DELETE
    @RolesAllowed({ RoleEnum.ROLE_TEACHER, RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_SUPER_ADMIN })
    @ActionDescription("Deleteou um curso")
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete/{uuid}")
    public Response deleteCourse(@PathParam("uuid") UUID uuid) {
        return courseBO.deleteCourse(uuid);
    }

    @GET
    @Path("generate/{uuid}/certification")
    @Produces("application/pdf")
    @RolesAllowed(RoleEnum.ROLE_STUDENT)
    @ActionDescription("Gerou um certificado de conclusão de um curso")
    public Response generateCertification(@PathParam("uuid") UUID uuid) {
        return courseBO.generateCertification(uuid);
    }

    @GET
    @Path("avaliation/list/{courseUUID}")
    @Authenticated
    @ActionDescription("Listou as avalicações de um curso")
    public Response listAvaliation(@PathParam("courseUUID") UUID courseUUID) {
        return courseBO.listAvaliations(courseUUID);
    }


    @POST
    @Path("avaliate")
    @RolesAllowed(RoleEnum.ROLE_STUDENT)
    @ActionDescription("Avaliou um curso")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response avaliateCourse(CourseAvaliationFormDTO courseStudentDTO) {
        return courseBO.avaliateCourse(courseStudentDTO);
    }
}
