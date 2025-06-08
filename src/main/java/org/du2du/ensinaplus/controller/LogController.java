package org.du2du.ensinaplus.controller;

import org.du2du.ensinaplus.model.bo.impl.LogBO;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.security.ActionDescription;
import org.du2du.ensinaplus.security.RequireRole;
import org.du2du.ensinaplus.security.RequiredAuthentication;

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
    @RequiredAuthentication
    @RequireRole(RoleEnum.ROLE_TEACHER)
    @ActionDescription("Listou os logs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllLogs(@QueryParam("page") Integer page){
        return logBO.listAllLogs(page);
    }
}
