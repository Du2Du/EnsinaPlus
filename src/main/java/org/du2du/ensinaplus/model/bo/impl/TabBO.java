package org.du2du.ensinaplus.model.bo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dto.UserDTO;
import org.du2du.ensinaplus.model.dto.base.ResponseDTO;
import org.du2du.ensinaplus.model.dto.list.TabListDTO;
import org.du2du.ensinaplus.model.enums.RoleEnum;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@Dependent
public class TabBO {
  @Inject
  SessionBO sessionBO;

  public Response getList() {
    UserDTO dto = sessionBO.getUserDTO();
    List<TabListDTO> list = new ArrayList<>();
    if (dto.getRole().equals(RoleEnum.STUDENT) || dto.getRole().equals(RoleEnum.TEACHER)) {
      list.add(TabListDTO.builder().uuid(UUID.randomUUID()).url("/home").icon("pi pi-book").label("Meus cursos").build());
    }
    if(dto.getRole().equals(RoleEnum.TEACHER)){
      list.add(TabListDTO.builder().uuid(UUID.randomUUID()).url("/course/form").icon("pi pi-plus").label("Criar curso").build());
    }
    if(dto.getRole().equals(RoleEnum.STUDENT)){
      list.add(TabListDTO.builder().uuid(UUID.randomUUID()).url("/explore").icon("pi pi-search").label("Explorar").build());
    }

    if (dto.getRole().equals(RoleEnum.ADMIN) || dto.getRole().equals(RoleEnum.SUPER_ADMIN)) {
      list.add(TabListDTO.builder().uuid(UUID.randomUUID()).url("/audit").icon("pi pi-chart-bar").label("Logs").build());
      list.add(TabListDTO.builder().uuid(UUID.randomUUID()).url("/explore").icon("pi pi-search").label("Explorar").build());
    }
    if(dto.getRole().equals(RoleEnum.SUPER_ADMIN)){
      list.add(TabListDTO.builder().uuid(UUID.randomUUID()).url("/roles").icon("pi pi-users").label("Permissões").build());
    }
    return Response.ok(ResponseDTO.builder().data(list).build()).build();
  }
}
