package org.du2du.ensinaplus.model.dao.impl;

import java.util.Map;
import java.util.UUID;

import org.du2du.ensinaplus.model.entity.impl.CourseStudent;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.Dependent;

@Dependent
public class CourseStudentDAO implements PanacheRepositoryBase<CourseStudent, UUID> {
    public CourseStudent findEnrollByStudentUUID (UUID uuidStudent){
        return find("student = :uuidStudent", Map.of("uuidStudent", uuidStudent)).firstResult();
    }
}
