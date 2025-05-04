package org.du2du.ensinaplus;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public String hello() {
        return "hello";
    }

    @GET
    @Path("/secured")	
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloSecured() {
        return "hello secured";
    }
}