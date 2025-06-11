package org.du2du.ensinaplus.controller;

import java.util.UUID;

import org.du2du.ensinaplus.model.bo.impl.ModuleResourceBO;
import org.du2du.ensinaplus.model.dto.FinalizeResourceDTO;
import org.du2du.ensinaplus.model.dto.form.ModuleResourceFormDTO;
import org.du2du.ensinaplus.model.dto.form.ModuleResourceUpdateFormDTO;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.security.ActionDescription;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/v1/resource")
public class ResourceController {

  @Inject
  ModuleResourceBO bo;

  @POST
  @Path("finalize")
  @ActionDescription("Finalizou o recurso")
  @RolesAllowed(RoleEnum.ROLE_STUDENT)
  public Response finalize(FinalizeResourceDTO dto) {
    return bo.finalize(dto);
  }

  @POST
  @Path("save")
  @RolesAllowed(RoleEnum.ROLE_TEACHER)
  @ActionDescription("Salvou o recurso")
  public Response save(ModuleResourceFormDTO dto) {
    return bo.save(dto);
  }

  @PUT
  @Path("update")
  @RolesAllowed(RoleEnum.ROLE_TEACHER)
  @ActionDescription("Atualizou o recurso")
  public Response update(ModuleResourceUpdateFormDTO dto) {
    return bo.update(dto);
  }

  @DELETE
  @Path("delete/{uuid}")
  @RolesAllowed({RoleEnum.ROLE_TEACHER, RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_SUPER_ADMIN})
  @ActionDescription("Deletou uma atividade")
  public Response delete(@PathParam("uuid") UUID moduleResourceUUID){
    return bo.delete(moduleResourceUUID);
  }
}
