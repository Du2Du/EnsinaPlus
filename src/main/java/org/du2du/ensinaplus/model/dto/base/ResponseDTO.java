package org.du2du.ensinaplus.model.dto.base;

import java.util.Objects;

import jakarta.ws.rs.core.Response.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResponseDTO<T> {
  private String title;
  private String description;
  private T data;
}
