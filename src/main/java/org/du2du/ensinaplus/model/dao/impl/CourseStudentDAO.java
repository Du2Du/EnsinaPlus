package org.du2du.ensinaplus.model.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.du2du.ensinaplus.model.dto.list.AvaliationListDTO;
import org.du2du.ensinaplus.model.entity.impl.CourseStudent;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.Dependent;

@Dependent
public class CourseStudentDAO implements PanacheRepositoryBase<CourseStudent, UUID> {

    public CourseStudent findEnroll(UUID uuidStudent, UUID uuidCourse) {
        return find("student.uuid = :uuidStudent AND course.uuid = :uuidCourse AND course.deleted = false",
                Map.of("uuidStudent", uuidStudent, "uuidCourse", uuidCourse)).firstResult();
    }

    public List<CourseStudent> listByCourse(UUID uuidCourse) {
        return find("course.uuid = :uuidCourse AND course.deleted = false",
                Map.of("uuidCourse", uuidCourse)).list();
    }

    public List<AvaliationListDTO> listAvaliations(UUID courseUUID) {
        return getEntityManager().createQuery(
                "select new org.du2du.ensinaplus.model.dto.list.AvaliationListDTO(cs.student.name, cs.avaliation, cs.stars) from CourseStudent cs where cs.course.uuid = :courseUUID and cs.avaliation is not null",
                AvaliationListDTO.class).setParameter("courseUUID", courseUUID).getResultList();
    }

}
