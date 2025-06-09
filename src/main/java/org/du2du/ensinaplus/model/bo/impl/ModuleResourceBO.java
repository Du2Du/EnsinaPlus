package org.du2du.ensinaplus.model.bo.impl;

import java.time.LocalDate;
import java.util.Objects;

import org.du2du.ensinaplus.model.bo.AbstractBO;
import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dao.impl.CourseDAO;
import org.du2du.ensinaplus.model.dao.impl.ModuleResourceDAO;
import org.du2du.ensinaplus.model.dto.base.ResponseDTO;
import org.du2du.ensinaplus.model.dto.base.ValidateDTO;
import org.du2du.ensinaplus.model.dto.form.ModuleResourceFormDTO;
import org.du2du.ensinaplus.model.dto.form.ModuleResourceUpdateFormDTO;
import org.du2du.ensinaplus.model.entity.impl.ModuleResource;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Dependent
public class ModuleResourceBO extends AbstractBO<ModuleResource, ModuleResourceDAO> {

    @Inject
    SessionBO sessionBO;

    @Inject
    CourseBO courseBO;

    @Inject
    CourseDAO courseDAO;

    private static final String SESSION_COOKIE_NAME = "ensina-plus-session";

    @Transactional
    public Response save(ModuleResourceFormDTO dto) {
        ValidateDTO validateResp = validate(dto);
        if (!validateResp.isOk())
            return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();
        ModuleResource entity = dto.toEntity();
        try {
            dao.persistAndFlush(entity);
            return Response.status(Response.Status.CREATED)
                    .entity(ResponseDTO.builder().title("Recurso criado com sucesso!").data(dto).build())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResponseDTO.builder().title("Erro ao criar recurso!")
                            .description(e.getMessage()).build())
                    .build();
        }
    }

    @Transactional
    public Response update(ModuleResourceUpdateFormDTO dto) {
        ValidateDTO validateResp = validate(dto);
        if (!validateResp.isOk())
            return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();

        ModuleResource entity = dao.findById(dto.getUuid());
        if (Objects.isNull(entity))
            return Response.status(Response.Status.CONFLICT)
                    .entity(ResponseDTO.builder().title("Erro ao salvar recurso!")
                            .description("Recurso não encontrado.").build())
                    .build();

        entity.setName(dto.getName());
        entity.setDescriptionHTML(dto.getDescriptioHTML());
        entity.setFile(dto.getFile());
        entity.setVideo(dto.getVideo());
        entity.setDeleted(Objects.isNull(dto.getDisabled()) ? false : dto.getDisabled());
        if (entity.getDeleted())
            entity.setDisabledDate(LocalDate.now());

        try {
            dao.persistAndFlush(entity);
            return Response.status(Response.Status.CREATED)
                    .entity(ResponseDTO.builder().title("Recurso atualizado com sucesso!").data(dto).build())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResponseDTO.builder().title("Erro ao atualizar recurso!")
                            .description(e.getMessage()).build())
                    .build();
        }
    }
}
