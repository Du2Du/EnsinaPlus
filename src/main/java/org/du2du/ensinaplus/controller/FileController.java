package org.du2du.ensinaplus.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.du2du.ensinaplus.model.dto.FileUploadResponseDTO;
import org.du2du.ensinaplus.model.dto.base.ResponseDTO;
import org.du2du.ensinaplus.model.dto.form.FileUploadFormDTO;
import org.du2du.ensinaplus.model.enums.RoleEnum;
import org.du2du.ensinaplus.security.ActionDescription;
import org.jboss.resteasy.reactive.MultipartForm;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@jakarta.ws.rs.Path("/v1/file")
public class FileController {

  private static final String UPLOAD_DIR = "src/main/resources/uploads";
  private static final String FILES_DIR = UPLOAD_DIR + "/files";
  private static final String VIDEOS_DIR = UPLOAD_DIR + "/videos";

  @POST
  @jakarta.ws.rs.Path("/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed({ RoleEnum.ROLE_TEACHER, RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_SUPER_ADMIN })
  @ActionDescription("Upload de arquivo")
  public Response uploadFile(@MultipartForm FileUploadFormDTO form) {
    try {
      createDirectoriesIfNotExist();

      String originalFileName = form.file.getName();
      String fileExtension = getFileExtension(originalFileName);
      String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

      String targetDir = "video".equals(form.type) ? VIDEOS_DIR : FILES_DIR;
      Path targetPath = Paths.get(targetDir, uniqueFileName);

      Files.copy(form.file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

      String relativePath = ("video".equals(form.type) ? "videos/" : "files/") + uniqueFileName;

      return Response.ok()
          .entity(ResponseDTO.builder().data(FileUploadResponseDTO.builder()
              .filePath(relativePath)
              .name(uniqueFileName)
              .build()).build())
          .build();
    } catch (IOException e) {
      return Response.ok()
          .entity(ResponseDTO.builder()
              .title("Erro ao fazer upload do arquivo")
              .build())
          .build();
    }
  }

  @GET
  @jakarta.ws.rs.Path("/download/{type}/{fileName}")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response downloadFile(@PathParam("type") String type, @PathParam("fileName") String fileName) {
    try {
      String targetDir = "videos".equals(type) ? VIDEOS_DIR : FILES_DIR;
      Path filePath = Paths.get(targetDir, fileName);

      if (!Files.exists(filePath)) {
        return Response.ok()
            .entity(ResponseDTO.builder()
                .title("Arquivo não encontrado")
                .build())
            .build();
      }

      byte[] fileContent = Files.readAllBytes(filePath);
      String contentType = Files.probeContentType(filePath);
      if (contentType == null) {
        contentType = MediaType.APPLICATION_OCTET_STREAM;
      }

      return Response.ok(fileContent, contentType)
          .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
          .build();

    } catch (IOException e) {
      return Response.ok()
          .entity(ResponseDTO.builder()
              .title("Erro ao baixar arquivo")
              .build())
          .build();
    }
  }

  private void createDirectoriesIfNotExist() throws IOException {
    Files.createDirectories(Paths.get(FILES_DIR));
    Files.createDirectories(Paths.get(VIDEOS_DIR));
  }

  private String getFileExtension(String fileName) {
    int lastDotIndex = fileName.lastIndexOf('.');
    return lastDotIndex > 0 ? fileName.substring(lastDotIndex) : "";
  }
}