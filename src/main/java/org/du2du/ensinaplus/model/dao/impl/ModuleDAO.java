package org.du2du.ensinaplus.model.dao.impl;

import org.du2du.ensinaplus.model.entity.impl.Module;

import jakarta.enterprise.context.Dependent;

import java.util.List;
import java.util.UUID;

import org.du2du.ensinaplus.model.dao.AbstractDAO;
import org.du2du.ensinaplus.model.dto.ModuleDTO;

@Dependent
public class ModuleDAO extends AbstractDAO<Module>{

    public List<ModuleDTO> listModulesOfCourse(UUID courseUuid){
        String query = "SELECT new org.du2du.ensinaplus.model.dto.ModuleDTO(module.uuid, module.name, module.description, module.positionOrder) FROM Module as module WHERE module.course.uuid = :courseUuid AND module.deleted = false ORDER BY module.positionOrder ASC";
        List<ModuleDTO> dtos = getEntityManager().createQuery(query, ModuleDTO.class).setParameter("courseUuid", courseUuid).getResultList();
        return dtos;
    }
}
