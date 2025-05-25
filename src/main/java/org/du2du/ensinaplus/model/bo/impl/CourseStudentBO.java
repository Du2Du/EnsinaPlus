package org.du2du.ensinaplus.model.bo.impl;

import java.util.Objects;

import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dao.impl.CourseDAO;
import org.du2du.ensinaplus.model.dao.impl.CourseStudentDAO;
import org.du2du.ensinaplus.model.dto.CourseStudentDTO;
import org.du2du.ensinaplus.model.dto.base.ResponseDTO;
import org.du2du.ensinaplus.model.entity.impl.CourseStudent;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Dependent
public class CourseStudentBO {
    
    @Inject
    CourseStudentDAO courseStudentDAO;

    @Inject
    CourseDAO courseDAO;

    @Inject
    SessionBO sessionBO;

    @Transactional
    public Response matriculateUser (CourseStudentDTO courseStudentDTO){

        System.out.println(courseDAO.findByName(courseStudentDTO.getCourseName()).getUuid());
        CourseStudent enrollEntity = courseStudentDAO.findEnrollByStudentUUID(sessionBO.getSession().getData().getUuid());
        if(Objects.nonNull(enrollEntity))
            return Response.status(Response.Status.CONFLICT).entity(ResponseDTO.builder().title("Error ao matricular-se no curso: " + courseStudentDTO.getCourseName())
            .description("Usuário já matriculado").build()).build();
        
        enrollEntity = courseStudentDTO.toEntity(sessionBO.getSession().getData().getUuid(), courseDAO.findByName(courseStudentDTO.getCourseName()).getUuid());
        
        try{
            courseStudentDAO.persistAndFlush(enrollEntity);
        return Response.status(Response.Status.CREATED)
                .entity(ResponseDTO.builder().title("Sua matricula no curso "+courseStudentDTO.getCourseName()+" foi concluída!").data(enrollEntity).build())
                .build();
        } catch(Exception e){
            e.printStackTrace();
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao matricular-se").description(e.getMessage()).build())
                .build();
        }

    }
}
