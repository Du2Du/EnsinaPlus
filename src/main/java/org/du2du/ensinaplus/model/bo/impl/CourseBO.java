package org.du2du.ensinaplus.model.bo.impl;

import java.util.Objects;

import org.du2du.ensinaplus.model.bo.AbstractBO;
import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dao.impl.CourseDAO;
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

        courseEntity = course.toEntity(sessionBO.getSession().getData().getUuid());
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

    
}
