package org.du2du.ensinaplus.model.bo.impl;

import java.util.ArrayList;
import java.util.List;

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
    UserDTO dto = sessionBO.getSession().getData();
    List<TabListDTO> list = new ArrayList<>();
    if (dto.getRole().equals(RoleEnum.STUDENT) || dto.getRole().equals(RoleEnum.TEACHER)) {
      list.add(TabListDTO.builder().url("/home").icon("pi pi-book").label("Meus cursos").build());
    }
    if (dto.getRole().equals(RoleEnum.ADMIN)) {
      list.add(TabListDTO.builder().url("/audit").icon("pi pi-chart-bar").label("Logs").build());
    }
    return Response.ok(ResponseDTO.builder().data(list).build()).build();
  }
}
