package org.du2du.ensinaplus.controller;

import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.security.RequiredAuthentication;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {
    @Inject
    SessionBO sessionBO;

    @GET
    @Path("/secured")
    @RequiredAuthentication()
    @Produces(MediaType.TEXT_PLAIN)
    public String helloSecured() {
        return "hello "+ sessionBO.getSession().getData().getName();
    }
}