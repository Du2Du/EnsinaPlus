package org.du2du.ensinaplus.controller;

import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.security.RequiredAuthentication;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Path("v1/auth")
public class AuthController {

    @Inject
    SessionBO sessionBO;

    @GET
    @RequiredAuthentication
    @Path("/validate")
    public Response validate(@Context HttpHeaders headers) {
        return sessionBO.getSession(headers) != null ? Response.ok().build()
                : Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @RequiredAuthentication
    @Path("/logout")
    public Response logout(@Context HttpHeaders headers) {
        return Response.ok(sessionBO.deleteSession(headers)).build();
    }
}
