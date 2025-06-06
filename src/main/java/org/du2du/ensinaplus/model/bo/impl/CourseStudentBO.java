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
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Dependent
public class CourseStudentBO {
    
    @Inject
    CourseStudentDAO courseStudentDAO;

    @Inject
    CourseDAO courseDAO;

    @Inject
    SessionBO sessionBO;

    private static final String SESSION_COOKIE_NAME = "ensina-plus-session";

    @Transactional
    public Response matriculateUser (CourseStudentDTO courseStudentDTO, HttpHeaders headers){

        
        CourseStudent enrollEntity = courseStudentDAO.findEnroll(sessionBO.getSession(headers.getCookies().get((SESSION_COOKIE_NAME))).getData().getUuid(), courseStudentDTO.getCourseUuid());
        if(Objects.nonNull(enrollEntity))
            return Response.status(Response.Status.CONFLICT).entity(ResponseDTO.builder().title("Error ao matricular-se no curso")
            .description("Usuário já matriculado").build()).build();
        
        enrollEntity = courseStudentDTO.toEntity(sessionBO.getSession(headers.getCookies().get((SESSION_COOKIE_NAME))).getData().getUuid());
        
        try{
            enrollEntity = courseStudentDAO.getEntityManager().merge(enrollEntity);
            courseStudentDAO.persistAndFlush(enrollEntity);
        return Response.status(Response.Status.CREATED)
                .entity(ResponseDTO.builder().title("Sua matricula no curso foi concluída!").data(courseStudentDTO).build())
                .build();
        } catch(Exception e){
            e.printStackTrace();
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao matricular-se").description(e.getMessage()).build())
                .build();
        }

    }
    
}
