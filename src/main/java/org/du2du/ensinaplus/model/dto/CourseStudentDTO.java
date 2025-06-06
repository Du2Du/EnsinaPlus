package org.du2du.ensinaplus.model.dto;


import java.time.LocalDate;
import java.util.UUID;

import org.du2du.ensinaplus.model.entity.impl.Course;
import org.du2du.ensinaplus.model.entity.impl.CourseStudent;
import org.du2du.ensinaplus.model.entity.impl.CourseStudentId;
import org.du2du.ensinaplus.model.entity.impl.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseStudentDTO {

    private UUID courseUuid;

    public CourseStudent toEntity(UUID uuidStudent){
        return CourseStudent.builder()
        .id(CourseStudentId.builder().student(uuidStudent).course(courseUuid).build())
        .student(User.builder().uuid(uuidStudent).build())
        .course(Course.builder().uuid(courseUuid).build())
        .matriculationDate(LocalDate.now())
        .build();
    }

}
