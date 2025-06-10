package org.du2du.ensinaplus.controller;

import org.du2du.ensinaplus.model.bo.impl.TabBO;
import org.du2du.ensinaplus.security.NotRequiredAudit;
import org.du2du.ensinaplus.security.RequiredAuthentication;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Path("v1/tab")
@RequiredAuthentication
public class TabController {

  @Inject
  TabBO bo;

  @GET
  @Path("list")
  @NotRequiredAudit
  public Response getList(@Context HttpHeaders headers) {
    return bo.getList(headers);
  }
}
