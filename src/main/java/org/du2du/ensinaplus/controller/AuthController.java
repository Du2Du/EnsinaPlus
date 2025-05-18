package org.du2du.ensinaplus.controller;

import org.du2du.ensinaplus.security.RequiredAuthentication;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("v1/auth")
public class AuthController {
    
    @GET
    @RequiredAuthentication
    @Path("/validate")
    public Response validate() {
        return Response.ok().build();
    }
}
