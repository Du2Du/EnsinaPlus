package org.du2du.ensinaplus.controller;

import java.util.Set;

import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.security.RequireRole;
import org.du2du.ensinaplus.utils.TokenUtils;

import jakarta.annotation.security.PermitAll;
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
        return tokenUtils.generate(Set.of(RoleEnum.ADMIN.getValue()));
    }

    @GET
    @Path("/secured")	
    @RequireRole(RoleEnum.ROLE_STUDENT)
    @Produces(MediaType.TEXT_PLAIN)
    public String helloSecured() {
        return "hello secured";
    }
}