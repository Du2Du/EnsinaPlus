package org.du2du.ensinaplus.model.bo.impl;

import java.util.List;

import org.du2du.ensinaplus.model.dao.impl.LogDAO;
import org.du2du.ensinaplus.model.dto.LogDTO;
import org.du2du.ensinaplus.model.dto.base.ResponseDTO;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Dependent
public class LogBO {
    @Inject
    LogDAO logDAO;

    @Transactional
    public void createLog(LogDTO logDTO){
        
        try{
            logDAO.persistAndFlush(logDTO.toEntity());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public Response listAllLogs(Integer page){
        try {
            List<LogDTO> logsDTO = logDAO.listLogs(page);
            Long totalElements = logDAO.countAllLogs();
            return Response.status(Response.Status.OK)
                 .entity(ResponseDTO.builder().title("Logs listados com sucesso").data(logsDTO).total(totalElements).build())
                 .build();
        }
        catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao listar logs").description(e.getMessage()).build())
                .build();
        }
    }
}
