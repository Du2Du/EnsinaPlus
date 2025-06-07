package org.du2du.ensinaplus.model.dto.base;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginatedResponseDTO <T>{
    private List<T> dtos;
    private Integer page;
    private Long totalElements;
}
