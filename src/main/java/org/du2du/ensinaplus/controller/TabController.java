package org.du2du.ensinaplus.controller;

import org.du2du.ensinaplus.model.bo.impl.TabBO;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("v1/tab")
@Authenticated
public class TabController {

  @Inject
  TabBO bo;

  @GET
  @Path("list")
  public Response getList() {
    return bo.getList();
  }
}
