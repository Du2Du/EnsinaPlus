package org.du2du.ensinaplus.model.dto.form;

import java.io.File;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

import jakarta.ws.rs.core.MediaType;

public class FileUploadFormDTO {
  @RestForm("file")
  @PartType(MediaType.APPLICATION_OCTET_STREAM)
  public File file;

  @RestForm("type")
  public String type;
}