package org.du2du.ensinaplus.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileUploadResponseDTO {
  private String filePath;
  private String name;
}
