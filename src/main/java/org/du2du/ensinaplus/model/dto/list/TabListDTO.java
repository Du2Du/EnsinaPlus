package org.du2du.ensinaplus.model.dto.list;

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
 
  private String url;
  private String icon;
  private String label;
}
