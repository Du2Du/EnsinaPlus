package org.du2du.ensinaplus.controller;

import org.du2du.ensinaplus.model.bo.impl.LogBO;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.security.ActionDescription;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("v1/log")
public class LogController {
    @Inject
    LogBO logBO;

    @GET
    @Path("list")
    @ActionDescription("Listou os logs")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_SUPER_ADMIN})
    public Response listAllLogs(@QueryParam("page") Integer page){
        return logBO.listAllLogs(page);
    }
}
