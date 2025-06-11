package org.du2du.ensinaplus.controller;

import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.security.NotRequiredAudit;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("v1/auth")
public class AuthController {

    @Inject
    SessionBO sessionBO;

    @GET
    @Authenticated
    @NotRequiredAudit
    @Path("/validate")
    public Response validate() {
        return sessionBO.getSession() != null ? Response.ok().build()
                : Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Authenticated
    @NotRequiredAudit
    @Path("/logout")
    public Response logout() {
        sessionBO.deleteSession();
        return Response.ok().build();
    }
}
