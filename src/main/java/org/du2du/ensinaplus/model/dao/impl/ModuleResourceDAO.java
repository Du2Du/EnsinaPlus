package org.du2du.ensinaplus.model.dao.impl;

import java.util.Map;
import java.util.UUID;

import org.du2du.ensinaplus.model.dao.AbstractDAO;
import org.du2du.ensinaplus.model.entity.impl.ModuleResource;

import jakarta.enterprise.context.Dependent;

@Dependent
public class ModuleResourceDAO extends AbstractDAO<ModuleResource>{

     public Long countCourseActivities(UUID courseUuid){
        String query = "SELECT COUNT(a) FROM ModuleResource a WHERE a.module.course.uuid = :courseUuid AND a.deleted = false";
         return find(query, Map.of("courseUuid", courseUuid)).count();
    }

    
}
