package org.du2du.ensinaplus.controller;

import java.util.Set;

import org.du2du.ensinaplus.model.enums.RolesEnum;
import org.du2du.ensinaplus.utils.TokenUtils;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @Inject
    TokenUtils tokenUtils;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public String hello() {
        return tokenUtils.generate(Set.of(RolesEnum.ROLE_ADMIN));
    }

    @GET
    @Path("/secured")	
    @RolesAllowed(RolesEnum.ROLE_ADMIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String helloSecured() {
        return "hello secured";
    }
}