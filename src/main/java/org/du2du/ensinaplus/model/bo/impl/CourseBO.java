package org.du2du.ensinaplus.model.bo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import jakarta.ws.rs.core.Response;

@Dependent
public class CourseBO extends AbstractBO<Course, CourseDAO>{

    @Inject
    SessionBO sessionBO;

    @Transactional
    public Response createCourse(CourseFormDTO course){
        ValidateDTO validateResp = validate(course);
        if(!validateResp.isOk()) 
            return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();

        Course courseEntity = dao.findByName(course.getName());
        if (Objects.nonNull(courseEntity))
        return Response.status(Response.Status.CONFLICT)
            .entity(ResponseDTO.builder().title("Erro ao criar curso!")
                .description("Já existe um curso com esse nome cadastrado!").build())
            .build();

        courseEntity = course.toEntity(sessionBO.getSession().getUuid());
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

    public Response listEnrollmentCourses(){
        List<Course> coursesEntity = dao.listMyCourses(sessionBO.getSession().getData().getUuid());
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

    public Response listCreatedCourses(){
        List<Course> coursesEntity = dao.listCreatedCourses(sessionBO.getSession().getData().getUuid());
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

    
}
