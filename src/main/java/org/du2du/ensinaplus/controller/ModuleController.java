package org.du2du.ensinaplus.controller;

import java.util.UUID;

import org.du2du.ensinaplus.model.bo.impl.ModuleBO;
import org.du2du.ensinaplus.model.dto.form.ModuleFormDTO;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.security.ActionDescription;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
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
    @RolesAllowed(RoleEnum.ROLE_TEACHER)
    @ActionDescription("Criou um módulo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createModule(ModuleFormDTO moduleFormDTO,  @Context HttpHeaders headers){
        return moduleBO.createModule(moduleFormDTO, headers);
    }

    @PUT
    @Path("update")
    @RolesAllowed(RoleEnum.ROLE_TEACHER)
    @ActionDescription("Atualizou os dados básicos de um módulo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateModule(ModuleFormDTO moduleFormDTO,  @Context HttpHeaders headers){
        return moduleBO.updateModule(moduleFormDTO, headers);
    }

    @DELETE
    @Path("delete/{uuid}")
    @RolesAllowed(RoleEnum.ROLE_TEACHER)
    @ActionDescription("Deletou um módulo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteModule(@PathParam ("uuid") UUID uuid,  @Context HttpHeaders headers){
        return moduleBO.deleteModule(uuid, headers);
    }

    @GET
    @Path("list/{course_uuid}")
    @Authenticated
    @ActionDescription("Listou os módulos de um curso")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listModulesOfCourse(@PathParam("course_uuid") UUID courseUuid){
        return moduleBO.listModulesOfCourse(courseUuid);
    }
}
