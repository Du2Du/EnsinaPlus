package org.du2du.ensinaplus.model.dto.list;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TabListDTO {
 
  private UUID uuid;
  private String url;
  private String icon;
  private String label;
}
