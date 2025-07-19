package org.du2du.ensinaplus.model.bo.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.du2du.ensinaplus.model.bo.AbstractBO;
import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dao.impl.CourseDAO;
import org.du2du.ensinaplus.model.dao.impl.CourseStudentDAO;
import org.du2du.ensinaplus.model.dao.impl.ModuleResourceDAO;
import org.du2du.ensinaplus.model.dao.impl.UserResourceDAO;
import org.du2du.ensinaplus.model.dto.CourseDTO;
import org.du2du.ensinaplus.model.dto.UserDTO;
import org.du2du.ensinaplus.model.dto.base.ResponseDTO;
import org.du2du.ensinaplus.model.dto.base.ValidateDTO;
import org.du2du.ensinaplus.model.dto.form.CourseAvaliationFormDTO;
import org.du2du.ensinaplus.model.dto.form.CourseFormDTO;
import org.du2du.ensinaplus.model.entity.impl.Course;
import org.du2du.ensinaplus.model.entity.impl.CourseStudent;
import org.du2du.ensinaplus.model.enums.RoleEnum;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Dependent
public class CourseBO extends AbstractBO<Course, CourseDAO> {

    @Inject
    SessionBO sessionBO;

    @Inject
    CourseStudentDAO courseStudentDAO;

    @Inject
    UserResourceDAO userResourceDAO;

    @Inject
    ModuleResourceDAO moduleResourceDAO;

    @Transactional
    public Response createCourse(CourseFormDTO course) {
        ValidateDTO validateResp = validate(course);
        if (!validateResp.isOk())
            return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();

        Course courseEntity = dao.findByName(course.getName());
        if (Objects.nonNull(courseEntity))
            return Response.status(Response.Status.CONFLICT)
                    .entity(ResponseDTO.builder().title("Erro ao criar curso!")
                            .description("Já existe um curso com esse nome cadastrado!").build())
                    .build();

        courseEntity = course.toEntity(sessionBO.getUserDTO().getUuid());
        try {
            dao.persistAndFlush(courseEntity);
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

    @Transactional
    public ResponseDTO<?> updateStarsAvg(Course course) {
        List<CourseStudent> courseStudents = courseStudentDAO.listByCourse(course.getUuid());
        Integer totalStars = 0;
        for (CourseStudent courseStudent : courseStudents) {
            if (Objects.nonNull(courseStudent.getStars())) {
                totalStars += courseStudent.getStars();
            }
        }
        course.setAvaliationAvg(totalStars == 0 ? 0F : (totalStars / courseStudents.size()));
        try {
            dao.persistAndFlush(course);
            return ResponseDTO.builder().title("Média de estrelas atualizada com sucesso!").build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.builder().title("Erro ao atualizar media de avaliação do curso")
                    .description(e.getMessage()).build();
        }
    }

    public Response listAllCourses() {
        List<Course> coursesEntity = dao.listAllNotDeleted();
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

    public Response findCourse(UUID uuid) {
        Course courseEntity = dao.findById(uuid);
        if (Objects.isNull(courseEntity))
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ResponseDTO.builder().title("Curso não encontrado").build())
                    .build();

        CourseStudent courseStudent = courseStudentDAO.findEnroll(sessionBO.getUserDTO().getUuid(), uuid);

        return Response.status(Response.Status.OK)
                .entity(ResponseDTO.builder().title("Curso encontrado com sucesso")
                        .data(courseEntity.toDTO(Objects.nonNull(courseStudent) && Objects.nonNull(courseStudent.getConclusionDate()),
                                Objects.nonNull(courseStudent),   Objects.nonNull(courseStudent) && Objects.nonNull(courseStudent.getStars())))
                        .build())
                .build();
    }

    public Response searchCourse(String search, Integer page, Integer limit) {
        List<CourseDTO> coursesDTO = dao.search(search, page, limit, sessionBO.getUserDTO().getUuid());
        if (coursesDTO.isEmpty())
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ResponseDTO.builder().title("Nenhum curso encontrado").data(List.of()).total(0L).build())
                    .build();

        return Response.status(Response.Status.OK)
                .entity(ResponseDTO.builder().title("Cursos encontrados com sucesso").data(coursesDTO)
                        .total(dao.countOfSearch(search)).build())
                .build();
    }

    public Response listEnrollmentCourses() {
        List<Course> coursesEntity = dao.listMyCourses(
                sessionBO.getUserDTO().getUuid());
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

    public Response listCreatedCourses() {
        List<Course> coursesEntity = dao.listCreatedCourses(
                sessionBO.getUserDTO().getUuid());
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
    public Response updateCourse(CourseFormDTO course) {
        ValidateDTO validateResp = validate(course);
        if (!validateResp.isOk())
            return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();

        Course courseEntity = dao.findById(course.getUuid());

        courseEntity.setName(course.getName());
        courseEntity.setDescription(course.getDescription());
        courseEntity.setMainPicture(course.getMainPicture());
        courseEntity.setUpdatedAt(LocalDateTime.now());

        if (!courseEntity.getOwner().getUuid().equals((sessionBO.getUserDTO().getUuid()))) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(ResponseDTO.builder().title("Somente o dono do curso pode alterar dados do curso").build())
                    .build();
        }
        try {
            dao.persistAndFlush(courseEntity);
            return Response.status(Response.Status.OK)
                    .entity(ResponseDTO.builder().title("Curso atualizado com sucesso!").data(course).build())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResponseDTO.builder().title("Erro ao atualizar curso").description(e.getMessage()).build())
                    .build();
        }
    }

    @Transactional
    public Response deleteCourse(UUID uuid) {
        Course courseEntity = dao.findById(uuid);

        if (!courseEntity.getOwner().getUuid()
                .equals((sessionBO.getUserDTO().getUuid())) &&
                !sessionBO.getUserDTO().getRole().equals(RoleEnum.ADMIN)
                && !sessionBO.getUserDTO().getRole().equals(RoleEnum.SUPER_ADMIN)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(ResponseDTO.builder().title("Somente o dono do curso pode deletar o curso").build())
                    .build();
        }
        try {
            courseEntity.setDeleted(true);
            dao.persistAndFlush(courseEntity);
            return Response.status(Response.Status.OK)
                    .entity(ResponseDTO.builder().title("Curso excluido com sucesso!").build())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ResponseDTO.builder().title("Erro ao excluir curso").description(e.getMessage()).build())
                    .build();
        }
    }

    @Transactional
    public Response avaliateCourse(CourseAvaliationFormDTO courseStudentDTO) {
        ValidateDTO validateResp = validate(courseStudentDTO);
        if (!validateResp.isOk())
            return Response.status(Response.Status.BAD_REQUEST).entity(validateResp).build();

        Course course = dao.findById(courseStudentDTO.getCourseUUID());
        if (Objects.isNull(course))
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ResponseDTO.builder().title("Curso não encontrado").build())
                    .build();

        UserDTO userDTO = sessionBO.getUserDTO();
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
            this.updateStarsAvg(courseStudent.getCourse());

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

    public Response generateCertification(UUID uuid) {
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

            UserDTO userDTO = sessionBO.getUserDTO();
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

    public Response listAvaliations(UUID courseUUID) {
        return Response.status(Response.Status.OK)
                .entity(ResponseDTO.builder().data(courseStudentDAO.listAvaliations(courseUUID)).build())
                .build();
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

    @Transactional
    public ResponseDTO<?> verifyCourseConclusion(UUID courseUuid){
        UUID userUUID = sessionBO.getUserDTO().getUuid();
        
        Course courseEntity = dao.findById(courseUuid);
        if(Objects.isNull(courseEntity)) return ResponseDTO.builder().title("Curso inexistente").build();
        try {
            if(userResourceDAO.countConcludedActivities(courseUuid, userUUID) == moduleResourceDAO.countCourseActivities(courseUuid)){
                CourseStudent enrollEntity = courseStudentDAO.findEnroll(userUUID, courseUuid);
                enrollEntity.setConclusionDate(LocalDate.now());
                courseStudentDAO.persistAndFlush(enrollEntity);
                return ResponseDTO.builder().title("Curso concluído com sucesso!").build();
            } 
            return ResponseDTO.builder().title("Curso não concluído!").build();
        } catch (Exception e){
            return ResponseDTO.builder().title("Error ao verificar conclusão de curso").build();
        }
    }

}
