package org.du2du.ensinaplus.controller;

import java.util.List;
import java.util.UUID;

import org.du2du.ensinaplus.model.bo.impl.ModuleBO;
import org.du2du.ensinaplus.model.dto.ModuleDTO;
import org.du2du.ensinaplus.model.dto.form.ModuleFormDTO;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.security.ActionDescription;
import org.du2du.ensinaplus.security.RequireRole;
import org.du2du.ensinaplus.security.RequiredAuthentication;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("v1/module")
public class ModuleController {
    
    @Inject
    ModuleBO moduleBO;

    @POST
    @Path("create")
    @RequiredAuthentication
    @RequireRole(RoleEnum.ROLE_TEACHER)
    @ActionDescription("Criou um módulo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createModule(ModuleFormDTO moduleFormDTO,  @Context HttpHeaders headers){
        return moduleBO.createModule(moduleFormDTO, headers);
    }

    @PUT
    @Path("update")
    @RequiredAuthentication
    @RequireRole(RoleEnum.ROLE_TEACHER)
    @ActionDescription("Atualizou os dados básicos de um módulo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateModule(ModuleFormDTO moduleFormDTO,  @Context HttpHeaders headers){
        return moduleBO.updateModule(moduleFormDTO, headers);
    }

    @PUT
    @Path("reorder")
    @RequiredAuthentication
    @RequireRole(RoleEnum.ROLE_TEACHER)
    @ActionDescription("Atualizou os dados básicos de um módulo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reorderPositionsModules(List<ModuleDTO> modulesDtos,  @Context HttpHeaders headers){
        return moduleBO.reorderPositionsModules(modulesDtos, headers);
    }

    @DELETE
    @Path("delete/{uuid}")
    @RequiredAuthentication
    @RequireRole(RoleEnum.ROLE_TEACHER)
    @ActionDescription("Deletou um módulo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteModule(@PathParam ("uuid") UUID uuid,  @Context HttpHeaders headers){
        return moduleBO.deleteModule(uuid, headers);
    }

    @GET
    @Path("list/{course_uuid}")
    @RequiredAuthentication
    @ActionDescription("Listou os módulos de um curso")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listModulesOfCourse(@PathParam("course_uuid") UUID courseUuid){
        return moduleBO.listModulesOfCourse(courseUuid);
    }
}
