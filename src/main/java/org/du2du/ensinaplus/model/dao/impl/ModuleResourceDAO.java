package org.du2du.ensinaplus.model.dao.impl;

import java.util.List;
import java.util.UUID;

import org.du2du.ensinaplus.model.dao.AbstractDAO;
import org.du2du.ensinaplus.model.dto.list.ResourceListDTO;
import org.du2du.ensinaplus.model.entity.impl.ModuleResource;

import jakarta.enterprise.context.Dependent;

@Dependent
public class ModuleResourceDAO extends AbstractDAO<ModuleResource> {

  public List<ResourceListDTO> listByModule(UUID moduleUuid) {
    return getEntityManager().createQuery(
        "SELECT new org.du2du.ensinaplus.model.dto.list.ResourceListDTO(r) FROM ModuleResource r WHERE r.module.uuid = :moduleUuid and r.deleted is false order by r.createdAt",
        ResourceListDTO.class)
        .setParameter("moduleUuid", moduleUuid)
        .getResultList();
  }
}
