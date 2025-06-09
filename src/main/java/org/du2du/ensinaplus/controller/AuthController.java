package org.du2du.ensinaplus.controller;

import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.security.NotRequiredAudit;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Path("v1/auth")
public class AuthController {

    private static final String SESSION_COOKIE_NAME = "ensina-plus-session";

    @Inject
    SessionBO sessionBO;

    @GET
    @Authenticated
    @NotRequiredAudit
    @Path("/validate")
    public Response validate(@Context HttpHeaders headers) {
        return sessionBO.getSession(headers.getCookies().get((SESSION_COOKIE_NAME))) != null ? Response.ok().build()
                : Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Authenticated
    @NotRequiredAudit
    @Path("/logout")
    public Response logout(@Context HttpHeaders headers) {
        return Response.ok(sessionBO.deleteSession(headers.getCookies().get((SESSION_COOKIE_NAME)))).build();
    }
}
