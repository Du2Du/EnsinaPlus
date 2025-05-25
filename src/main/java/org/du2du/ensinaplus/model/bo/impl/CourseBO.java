package org.du2du.ensinaplus.model.bo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.du2du.ensinaplus.model.bo.AbstractBO;
import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dao.impl.CourseDAO;
import org.du2du.ensinaplus.model.dto.CourseDTO;
import org.du2du.ensinaplus.model.dto.base.ResponseDTO;
import org.du2du.ensinaplus.model.dto.base.ValidateDTO;
import org.du2du.ensinaplus.model.dto.form.CourseFormDTO;
import org.du2du.ensinaplus.model.entity.impl.Course;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Dependent
public class CourseBO extends AbstractBO<Course, CourseDAO>{

    @Inject
    SessionBO sessionBO;

    private static final String SESSION_COOKIE_NAME = "ensina-plus-session";

    @Transactional
    public Response createCourse(CourseFormDTO course, HttpHeaders headers){
        ValidateDTO validateResp = validate(course);
        if(!validateResp.isOk()) 
            return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();

        Course courseEntity = dao.findByName(course.getName());
        if (Objects.nonNull(courseEntity))
        return Response.status(Response.Status.CONFLICT)
            .entity(ResponseDTO.builder().title("Erro ao criar curso!")
                .description("Já existe um curso com esse nome cadastrado!").build())
            .build();

        courseEntity = course.toEntity(sessionBO.getSession(headers.getCookies().get((SESSION_COOKIE_NAME))).getData().getUuid());
        try{
            courseEntity.persistAndFlush();
            return Response.status(Response.Status.CREATED)
                .entity(ResponseDTO.builder().title("Curso criado com sucesso!").data(course).build())
                .build();
        } catch(Exception e){
            e.printStackTrace();
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao criar curso").description(e.getMessage()).build())
                .build();
        }
    }

    public Response listAllCourses(){
        List<Course> coursesEntity = dao.listAll();
        List<CourseDTO> coursesDTO = new ArrayList<>();
        coursesEntity.forEach((course)->{coursesDTO.add(course.toDTO());});
        try {
            return Response.status(Response.Status.OK)
                .entity(ResponseDTO.builder().title("Cursos listados com sucesso").data(coursesDTO).build())
                .build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao listar cursos").description(e.getMessage()).build())
                .build();
        }
    }

    public Response listEnrollmentCourses(HttpHeaders headers){
        List<Course> coursesEntity = dao.listMyCourses(sessionBO.getSession(headers.getCookies().get((SESSION_COOKIE_NAME))).getData().getUuid());
        List<CourseDTO> coursesDTO = new ArrayList<>();
        coursesEntity.forEach((course)->{coursesDTO.add(course.toDTO());});
        try {
            return Response.status(Response.Status.OK)
                .entity(ResponseDTO.builder().title("Meus cursos inscritos listados com sucesso").data(coursesDTO).build())
                .build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao listar cursos").description(e.getMessage()).build())
                .build();
        }
    }

    public Response listCreatedCourses(HttpHeaders headers){
        List<Course> coursesEntity = dao.listCreatedCourses(sessionBO.getSession(headers.getCookies().get((SESSION_COOKIE_NAME))).getData().getUuid());
        List<CourseDTO> coursesDTO = new ArrayList<>();
        coursesEntity.forEach((course)->{coursesDTO.add(course.toDTO());});
        try {
            return Response.status(Response.Status.OK)
                .entity(ResponseDTO.builder().title("Meus cursos criados listados com sucesso").data(coursesDTO).build())
                .build();
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao listar cursos").description(e.getMessage()).build())
                .build();
        }
    }

    @Transactional
    public Response updateCourse(CourseFormDTO course, HttpHeaders headers, UUID uuid){
        ValidateDTO validateResp = validate(course);
        if(!validateResp.isOk()) 
            return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();

        Course courseEntity = dao.findById(uuid);
    
        courseEntity.setName(course.getName());
        courseEntity.setDescription(course.getDescription());
        courseEntity.setMainPicture(course.getMainPicture());

         if (!dao.findById(uuid).getOwner().getUuid().equals((sessionBO.getSession(headers.getCookies().get((SESSION_COOKIE_NAME))).getData().getUuid()))){
            return Response.status(Response.Status.FORBIDDEN)
                .entity(ResponseDTO.builder().title("Somente o dono do curso pode alterar dados do curso").build())
                .build();
        }
        try{
            courseEntity.persistAndFlush();
            return Response.status(Response.Status.OK)
                .entity(ResponseDTO.builder().title("Curso atualizado com sucesso!").data(course).build())
                .build();
        } catch(Exception e){
            e.printStackTrace();
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao atualizar curso").description(e.getMessage()).build())
                .build();
        }
    }

    @Transactional
    public Response deleteCourse(UUID uuid, HttpHeaders headers){
        Course courseEntity = dao.findById(uuid);
        
        if (!courseEntity.getOwner().getUuid().equals((sessionBO.getSession(headers.getCookies().get((SESSION_COOKIE_NAME))).getData().getUuid()))){
            return Response.status(Response.Status.FORBIDDEN)
                .entity(ResponseDTO.builder().title("Somente o dono do curso pode deletar o curso").build())
                .build();
        }
        try{
            courseEntity.setDeleted(true);
           courseEntity.persistAndFlush();
            return Response.status(Response.Status.OK)
                .entity(ResponseDTO.builder().title("Curso excluido com sucesso!").build())
                .build();
        } catch(Exception e){
            e.printStackTrace();
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao excluir curso").description(e.getMessage()).build())
                .build();
        }
    }

    
}
