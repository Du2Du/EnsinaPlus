package org.du2du.ensinaplus.model.bo.impl;

import java.util.Objects;
import java.util.UUID;

import org.du2du.ensinaplus.model.bo.AbstractBO;
import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dao.impl.CourseDAO;
import org.du2du.ensinaplus.model.dao.impl.ModuleResourceDAO;
import org.du2du.ensinaplus.model.dao.impl.UserResourceDAO;
import org.du2du.ensinaplus.model.dto.FinalizeResourceDTO;
import org.du2du.ensinaplus.model.dto.base.ResponseDTO;
import org.du2du.ensinaplus.model.dto.base.ValidateDTO;
import org.du2du.ensinaplus.model.dto.form.ModuleResourceFormDTO;
import org.du2du.ensinaplus.model.dto.form.ModuleResourceUpdateFormDTO;
import org.du2du.ensinaplus.model.entity.impl.ModuleResource;
import org.du2du.ensinaplus.model.entity.impl.User;
import org.du2du.ensinaplus.model.entity.impl.UserResource;
import org.du2du.ensinaplus.model.enums.RoleEnum;

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

    @Inject
    UserResourceDAO userResourceDAO;

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
        entity.setDescriptionHTML(dto.getDescriptionHTML());
        entity.setFile(dto.getFile());
        entity.setVideo(dto.getVideo());

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

    @Transactional
    public Response delete(UUID moduleResourceUUID){
        ModuleResource moduleResourceEntity = dao.findById(moduleResourceUUID);
        if (Objects.isNull(moduleResourceEntity)){
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ResponseDTO.builder().title("Erro ao excluir atividade!").description("Atividade inexistente").build())
                .build();
        }
        if (!moduleResourceEntity.getModule().getCourse().getOwner().getUuid().equals((sessionBO.getUserDTO().getUuid())) &&
                !sessionBO.getUserDTO().getRole().equals(RoleEnum.ADMIN)
                && !sessionBO.getUserDTO().getRole().equals(RoleEnum.SUPER_ADMIN) ){
            return Response.status(Response.Status.FORBIDDEN)
                .entity(ResponseDTO.builder().title("Somente o dono do curso ou usuário papel superior pode deletar uma atividade").build())
                .build();
        }
        try{
            moduleResourceEntity.setDeleted(true);
            dao.persistAndFlush(moduleResourceEntity);
            return Response.status(Response.Status.OK)
                .entity(ResponseDTO.builder().title("Atividade deletada com sucesso!").build())
                .build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao deletar atividade").description(e.getMessage()).build())
                .build();
        }
    }

    @Transactional
    public Response finalize(FinalizeResourceDTO dto) {
        ModuleResource entity = dao.findById(dto.getResourceUUID());
        if (Objects.isNull(entity))
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ResponseDTO.builder().title("Erro ao finalizar recurso!")
                            .description("Recurso não encontrado.").build())
                    .build();

        if (Objects.nonNull(
                userResourceDAO.findByUserAndResource(sessionBO.getUserDTO().getUuid(), dto.getResourceUUID())))
            return Response.status(Response.Status.CONFLICT)
                    .entity(ResponseDTO.builder().title("Erro ao finalizar recurso!")
                            .description("Recurso já finalizado.").build())
                    .build();

        UserResource userResource = UserResource.builder()
                .user(User.builder().uuid(sessionBO.getUserDTO().getUuid()).build()).resource(entity).build();
        try {
            userResourceDAO.persistAndFlush(userResource);
            courseBO.verifyCourseConclusion(entity.getModule().getCourse().getUuid());
            return Response.status(Response.Status.OK)
                    .entity(ResponseDTO.builder().title("Recurso finalizado com sucesso!").data(dto).build())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResponseDTO.builder().title("Erro ao finalizar recurso!")
                            .description(e.getMessage()).build())
                    .build();
        }
    }
}
