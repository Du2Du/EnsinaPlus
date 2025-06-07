package org.du2du.ensinaplus.controller;

import java.util.UUID;

import org.du2du.ensinaplus.model.bo.impl.ModuleBO;
import org.du2du.ensinaplus.model.dto.form.ModuleFormDTO;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.security.RequireRole;
import org.du2du.ensinaplus.security.RequiredAuthentication;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Path("v1/module")
public class ModuleController {
    
    @Inject
    ModuleBO moduleBO;

    @POST
    @Path("create")
    @RequiredAuthentication
    @RequireRole(RoleEnum.ROLE_TEACHER)
    public Response createModule(ModuleFormDTO moduleFormDTO,  @Context HttpHeaders headers){
        return moduleBO.createModule(moduleFormDTO, headers);
    }

    @PUT
    @Path("update")
    @RequiredAuthentication
    @RequireRole(RoleEnum.ROLE_TEACHER)
    public Response updateModule(ModuleFormDTO moduleFormDTO,  @Context HttpHeaders headers){
        return moduleBO.updateModule(moduleFormDTO, headers);
    }

    @DELETE
    @Path("delete/{uuid}")
    @RequiredAuthentication
    @RequireRole(RoleEnum.ROLE_TEACHER)
    public Response createModule(@PathParam ("uuid") UUID uuid,  @Context HttpHeaders headers){
        return moduleBO.deleteModule(uuid, headers);
    }
}
