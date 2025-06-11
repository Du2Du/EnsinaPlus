package org.du2du.ensinaplus.model.bo.impl;

import java.util.Objects;
import java.util.UUID;

import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dao.impl.CourseDAO;
import org.du2du.ensinaplus.model.dao.impl.CourseStudentDAO;
import org.du2du.ensinaplus.model.dao.impl.UserResourceDAO;
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

    @Inject
    UserResourceDAO userResourceDAO;

    @Transactional
    public Response matriculateUser(CourseStudentDTO courseStudentDTO) {
        CourseStudent enrollEntity = courseStudentDAO.findEnroll(sessionBO.getUserDTO().getUuid(),
                courseStudentDTO.getCourseUuid());
        if (Objects.nonNull(enrollEntity))
            return Response.status(Response.Status.CONFLICT)
                    .entity(ResponseDTO.builder().title("Error ao matricular-se no curso")
                            .description("Usuário já matriculado").build())
                    .build();

        enrollEntity = courseStudentDTO.toEntity(sessionBO.getUserDTO().getUuid());

        try {
            enrollEntity = courseStudentDAO.getEntityManager().merge(enrollEntity);
            courseStudentDAO.persistAndFlush(enrollEntity);
            return Response.status(Response.Status.CREATED)
                    .entity(ResponseDTO.builder().title("Sua matricula no curso foi concluída!").data(courseStudentDTO)
                            .build())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResponseDTO.builder().title("Erro ao matricular-se").description(e.getMessage()).build())
                    .build();
        }

    }

    @Transactional
    public Response desmatricularCurso(UUID courseUUID) {
        CourseStudent enrollEntity = courseStudentDAO.findEnroll(sessionBO.getUserDTO().getUuid(), courseUUID);
        if (Objects.isNull(enrollEntity))
            return Response.status(Response.Status.CONFLICT)
                    .entity(ResponseDTO.builder().title("Error ao desmatricular-se do curso.")
                            .description("Curso não encontrado ou usuário não está matriculado!").build())
                    .build();

        try {
            courseStudentDAO.delete(enrollEntity);
            userResourceDAO.deleteUserResources(sessionBO.getUserDTO().getUuid(), courseUUID);
            courseStudentDAO.flush();
            return Response.status(Response.Status.CREATED)
                    .entity(ResponseDTO.builder().title("Sua matricula no curso foi cancelada!").data(courseUUID)
                            .build())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResponseDTO.builder().title("Erro ao desmatricular-se").description(e.getMessage()).build())
                    .build();
        }
    }

}
