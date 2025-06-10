package org.du2du.ensinaplus.controller;

import org.du2du.ensinaplus.model.bo.impl.ModuleResourceBO;
import org.du2du.ensinaplus.model.dto.FinalizeResourceDTO;
import org.du2du.ensinaplus.model.dto.form.ModuleResourceFormDTO;
import org.du2du.ensinaplus.model.dto.form.ModuleResourceUpdateFormDTO;
import org.du2du.ensinaplus.model.enums.RoleEnum;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/v1/resource")
public class ResourceController {

  @Inject
  ModuleResourceBO bo;

  @POST
  @Path("finalize")
  @RolesAllowed(RoleEnum.ROLE_STUDENT)
  public Response finalize(FinalizeResourceDTO dto) {
    return bo.finalize(dto);
  }

  @POST
  @Path("save")
  @RolesAllowed(RoleEnum.ROLE_STUDENT)
  public Response save(ModuleResourceFormDTO dto) {
    return bo.save(dto);
  }

  @PUT
  @Path("update")
  @RolesAllowed(RoleEnum.ROLE_STUDENT)
  public Response update(ModuleResourceUpdateFormDTO dto) {
    return bo.update(dto);
  }
}
