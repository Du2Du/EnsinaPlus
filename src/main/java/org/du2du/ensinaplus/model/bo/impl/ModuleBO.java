package org.du2du.ensinaplus.model.bo.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.du2du.ensinaplus.model.bo.AbstractBO;
import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dao.impl.CourseDAO;
import org.du2du.ensinaplus.model.dao.impl.ModuleDAO;
import org.du2du.ensinaplus.model.dto.ModuleDTO;
import org.du2du.ensinaplus.model.dto.base.ResponseDTO;
import org.du2du.ensinaplus.model.dto.base.ValidateDTO;
import org.du2du.ensinaplus.model.dto.form.ModuleFormDTO;
import org.du2du.ensinaplus.model.entity.impl.Module;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Dependent
public class ModuleBO extends AbstractBO<Module, ModuleDAO>{

    @Inject
    SessionBO sessionBO;

    @Inject
    CourseBO courseBO;

    @Inject
    CourseDAO courseDAO; 
    
    private static final String SESSION_COOKIE_NAME = "ensina-plus-session";

    @Transactional
    public Response createModule(ModuleFormDTO moduleFormDTO, HttpHeaders headers){
        ValidateDTO validateResp = validate(moduleFormDTO);
        if (!validateResp.isOk())
            return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();

        if (!courseDAO.findById(moduleFormDTO.getCourseUuid()).getOwner().getUuid().equals((sessionBO.getSession(headers.getCookies().get((SESSION_COOKIE_NAME))).getData().getUuid()))){
            return Response.status(Response.Status.FORBIDDEN)
                .entity(ResponseDTO.builder().title("Somente o dono do curso pode criar módulos nesse curso").build())
                .build();
        }
        Module moduleEntity = moduleFormDTO.toEntity();
        moduleEntity.setCreatedAt(LocalDateTime.now());
         
        try{
            dao.persistAndFlush(moduleEntity);
            return Response.status(Response.Status.CREATED)
                .entity(ResponseDTO.builder().title("Módulo criado com sucesso!").data(moduleFormDTO).build())
                .build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao criar módulo").data(e.getMessage()).build())
                .build();
        }
    }

    @Transactional
    public Response updateModule(ModuleFormDTO moduleFormDTO, HttpHeaders headers){
        ValidateDTO validateResp = validate(moduleFormDTO);
        if (!validateResp.isOk())
            return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();

        Module moduleEntity = dao.findById(moduleFormDTO.getUuid());
        moduleEntity.setName(moduleFormDTO.getName());
        moduleEntity.setDescription(moduleFormDTO.getDescription());
        moduleEntity.setUpdatedAt(LocalDateTime.now());
       
        if (!moduleEntity.getCourse().getOwner().getUuid().equals((sessionBO.getSession(headers.getCookies().get((SESSION_COOKIE_NAME))).getData().getUuid()))){
            return Response.status(Response.Status.FORBIDDEN)
                .entity(ResponseDTO.builder().title("Somente o dono do curso pode criar módulos nesse curso").build())
                .build();
        }
        try{
            dao.persistAndFlush(moduleEntity);
            return Response.status(Response.Status.CREATED)
                .entity(ResponseDTO.builder().title("Módulo atualizado com sucesso!").data(moduleFormDTO).build())
                .build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao atualizar módulo").data(e.getMessage()).build())
                .build();
        }
    }

    @Transactional
    public Response deleteModule(UUID uuidModule, HttpHeaders headers){
        Module moduleEntity = dao.findById(uuidModule);
        if (Objects.isNull(moduleEntity)){
            return Response.status(Response.Status.CREATED)
                .entity(ResponseDTO.builder().title("Erro ao excluir módulo!").description("Módulo inexistente").build())
                .build();
        }
        if (!moduleEntity.getCourse().getOwner().getUuid().equals((sessionBO.getSession(headers.getCookies().get((SESSION_COOKIE_NAME))).getData().getUuid()))){
            return Response.status(Response.Status.FORBIDDEN)
                .entity(ResponseDTO.builder().title("Somente o dono do curso pode deletar um módulo").build())
                .build();
        }
        try{
            moduleEntity.setDeleted(true);
            dao.persistAndFlush(moduleEntity);
            return Response.status(Response.Status.CREATED)
                .entity(ResponseDTO.builder().title("Módulo deletado com sucesso!").build())
                .build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao deletado módulo").description(e.getMessage()).build())
                .build();
        }
    }

    public Response listModulesOfCourse(UUID courseUuid){
        try {
            List<ModuleDTO> moduleDTOs = dao.listModulesOfCourse(courseUuid);
            return Response.status(Response.Status.OK)
                    .entity(ResponseDTO.builder().title("Módulos listados com sucesso!").data(moduleDTOs).build())
                    .build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao listar módulos").description(e.getMessage()).build())
                .build();
        }
    }
}
