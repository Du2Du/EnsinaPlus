package org.du2du.ensinaplus.model.dao.impl;

import java.util.List;
import java.util.UUID;

import org.du2du.ensinaplus.model.dto.LogDTO;
import org.du2du.ensinaplus.model.dto.base.PaginatedResponseDTO;
import org.du2du.ensinaplus.model.entity.impl.Log;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.Dependent;

@Dependent
public class LogDAO implements PanacheRepositoryBase<Log, UUID>{
    
    public PaginatedResponseDTO<LogDTO> listLogs(Integer page){
        Integer offset = page * 10;
        String query = "SELECT new org.du2du.ensinaplus.model.dto.LogDTO(log) FROM Log as log ORDER BY log.createdAt DESC";
        List<LogDTO> dtos = getEntityManager().createQuery(query, LogDTO.class).setFirstResult(offset).setMaxResults(10).getResultList();
        Long totalElements = getEntityManager().createQuery("SELECT COUNT(*) FROM Log", Long.class).getSingleResult();
        return new PaginatedResponseDTO<>(dtos, 0, totalElements);


    }
}
