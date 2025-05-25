package org.du2du.ensinaplus.model.dao.impl;

import java.util.Map;
import java.util.UUID;

import org.du2du.ensinaplus.model.entity.impl.CourseStudent;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.Dependent;
import jakarta.transaction.Transactional;

@Dependent
public class CourseStudentDAO implements PanacheRepositoryBase<CourseStudent, UUID> {

    public CourseStudent findEnroll (UUID uuidStudent, UUID uuidCourse){
        return find("student.uuid = :uuidStudent AND course.uuid = :uuidCourse", Map.of("uuidStudent", uuidStudent, "uuidCourse", uuidCourse)).firstResult();
    }
    
}
