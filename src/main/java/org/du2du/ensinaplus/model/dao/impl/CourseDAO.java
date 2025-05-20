package org.du2du.ensinaplus.model.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.du2du.ensinaplus.model.dao.AbstractDAO;
import org.du2du.ensinaplus.model.entity.impl.Course;

import jakarta.enterprise.context.Dependent;

@Dependent
public class CourseDAO extends AbstractDAO<Course>{

    public Course findByName(String name){
        return find("name LIKE '%' || :name || '%'", Map.of("name", name)).firstResult();
    }

    public List<Course> listByName(String name){
        return find("name LIKE '%' || :name || '%'", Map.of("name", name)).list();
    }

    public List<Course> listCreatedCourses (UUID uuidUser){
        return find("owner.uuid = :uuidUser", Map.of("uuidUser", uuidUser)).list();
    }

    public List<Course> listMyCourses (UUID uuidUser){
        return find("students WHERE students.uuid = :uuidUser", Map.of("uuidUser", uuidUser)).list();
    }
    
}
