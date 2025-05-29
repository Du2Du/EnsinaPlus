package org.du2du.ensinaplus.model.bo.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.du2du.ensinaplus.model.bo.AbstractBO;
import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dao.impl.CourseDAO;
import org.du2du.ensinaplus.model.dao.impl.CourseStudentDAO;
import org.du2du.ensinaplus.model.dto.CourseDTO;
import org.du2du.ensinaplus.model.dto.UserDTO;
import org.du2du.ensinaplus.model.dto.base.ResponseDTO;
import org.du2du.ensinaplus.model.dto.base.ValidateDTO;
import org.du2du.ensinaplus.model.dto.form.CourseAvaliationFormDTO;
import org.du2du.ensinaplus.model.dto.form.CourseFormDTO;
import org.du2du.ensinaplus.model.entity.impl.Course;
import org.du2du.ensinaplus.model.entity.impl.CourseStudent;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Dependent
public class CourseBO extends AbstractBO<Course, CourseDAO> {

    @Inject
    SessionBO sessionBO;

    @Inject
    CourseStudentDAO courseStudentDAO;

    @Transactional
    public Response createCourse(CourseFormDTO course, HttpHeaders headers) {
        ValidateDTO validateResp = validate(course);
        if (!validateResp.isOk())
            return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();

        Course courseEntity = dao.findByName(course.getName());
        if (Objects.nonNull(courseEntity))
            return Response.status(Response.Status.CONFLICT)
                    .entity(ResponseDTO.builder().title("Erro ao criar curso!")
                            .description("Já existe um curso com esse nome cadastrado!").build())
                    .build();

        courseEntity = course.toEntity(sessionBO.getSession(headers).getData().getUuid());
        try {
            courseEntity.persistAndFlush();
            return Response.status(Response.Status.CREATED)
                    .entity(ResponseDTO.builder().title("Curso criado com sucesso!").data(course).build())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResponseDTO.builder().title("Erro ao criar curso").description(e.getMessage()).build())
                    .build();
        }
    }

    public Response listAllCourses() {
        List<Course> coursesEntity = dao.listAll();
        List<CourseDTO> coursesDTO = new ArrayList<>();
        coursesEntity.forEach((course) -> {
            coursesDTO.add(course.toDTO());
        });
        try {
            return Response.status(Response.Status.OK)
                    .entity(ResponseDTO.builder().title("Cursos listados com sucesso").data(coursesDTO).build())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResponseDTO.builder().title("Erro ao listar cursos").description(e.getMessage()).build())
                    .build();
        }
    }

    public Response listEnrollmentCourses(HttpHeaders headers) {
        List<Course> coursesEntity = dao.listMyCourses(sessionBO.getSession(headers).getData().getUuid());
        List<CourseDTO> coursesDTO = new ArrayList<>();
        coursesEntity.forEach((course) -> {
            coursesDTO.add(course.toDTO());
        });
        try {
            return Response.status(Response.Status.OK)
                    .entity(ResponseDTO.builder().title("Meus cursos inscritos listados com sucesso").data(coursesDTO)
                            .build())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResponseDTO.builder().title("Erro ao listar cursos").description(e.getMessage()).build())
                    .build();
        }
    }

    public Response listCreatedCourses(HttpHeaders headers) {
        List<Course> coursesEntity = dao.listCreatedCourses(sessionBO.getSession(headers).getData().getUuid());
        List<CourseDTO> coursesDTO = new ArrayList<>();
        coursesEntity.forEach((course) -> {
            coursesDTO.add(course.toDTO());
        });
        try {
            return Response.status(Response.Status.OK)
                    .entity(ResponseDTO.builder().title("Meus cursos criados listados com sucesso").data(coursesDTO)
                            .build())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResponseDTO.builder().title("Erro ao listar cursos").description(e.getMessage()).build())
                    .build();
        }
    }

    @Transactional
    public Response avaliateCourse(CourseAvaliationFormDTO courseStudentDTO, HttpHeaders headers) {
        ValidateDTO validateResp = validate(courseStudentDTO);
        if (!validateResp.isOk())
            return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();

        Course course = dao.findById(courseStudentDTO.getCourseUUID());
        if (Objects.isNull(course))
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ResponseDTO.builder().title("Curso não encontrado").build())
                    .build();

        UserDTO userDTO = sessionBO.getSession(headers).getData();
        CourseStudent courseStudent = courseStudentDAO.findEnroll(userDTO.getUuid(), course.getUuid());
        if (Objects.isNull(courseStudent))
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ResponseDTO.builder().title("Você não está matriculado nesse curso").build())
                    .build();

        if (Objects.nonNull(courseStudent.getStars()))
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ResponseDTO.builder().title("Você já avaliou esse curso").build())
                    .build();

        courseStudent.setStars(courseStudentDTO.getStars());
        courseStudent.setAvaliation(courseStudentDTO.getComment());
        try {
            courseStudentDAO.persistAndFlush(courseStudent);
            return Response.status(Response.Status.OK)
                    .entity(ResponseDTO.builder().title("Avaliação realizada com sucesso").build())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResponseDTO.builder().title("Erro ao realizar avaliação").description(e.getMessage())
                            .build())
                    .build();
        }
    }

    public Response generateCertification(HttpHeaders headers, UUID uuid) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            String htmlTemplate = loadCertificateTemplate();

            Course course = dao.findById(uuid);
            if (Objects.isNull(course)) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ResponseDTO.builder().title("Curso não encontrado").build())
                        .build();
            }

            UserDTO userDTO = sessionBO.getSession(headers).getData();
            CourseStudent courseStudent = courseStudentDAO.findEnroll(userDTO.getUuid(), course.getUuid());
            String htmlContent = replaceCertificateVariables(htmlTemplate, userDTO.getName(), course,
                    courseStudent.getMatriculationDate(), courseStudent.getConclusionDate());

            try (InputStream fontStream = getClass().getClassLoader()
                    .getResourceAsStream("fonts/PlayfairDisplay-Medium.ttf")) {
                if (Objects.nonNull(fontStream)) {
                    builder.useFont(() -> fontStream, "Playfair Display", 400, BaseRendererBuilder.FontStyle.NORMAL,
                            true);
                }
            }

            builder.withHtmlContent(htmlContent, null);
            builder.toStream(os);
            builder.run();

            return Response.ok(os.toByteArray())
                    .header("Content-Disposition", "attachment; filename=\"certificate.pdf\"")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResponseDTO.builder().title("Erro ao gerar certificado").description(e.getMessage())
                            .build())
                    .build();
        }
    }

    private String loadCertificateTemplate() throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("templates/certificado.html")) {
            if (Objects.isNull(is)) {
                throw new IOException("Template de certificado não encontrado");
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private String replaceCertificateVariables(String template, String studentName, Course course, LocalDate startDate,
            LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String currentDate = LocalDate.now().format(formatter);

        return template
                .replace("__nome_estudante__", studentName)
                .replace("__nome_curso__", course.getName())
                .replace("__data_inicio__", startDate.format(formatter))
                .replace("__data_final__", endDate.format(formatter))
                .replace("__data_emissao__", currentDate);
    }

}
