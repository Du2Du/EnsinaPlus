package org.du2du.ensinaplus.model.bo.impl;

import org.du2du.ensinaplus.model.dao.impl.LogDAO;
import org.du2du.ensinaplus.model.dto.LogDTO;
import org.du2du.ensinaplus.model.dto.base.PaginatedResponseDTO;
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
            PaginatedResponseDTO<LogDTO> logsDTO = logDAO.listLogs(page);
            return Response.status(Response.Status.OK)
                 .entity(ResponseDTO.builder().title("Logs listados com sucesso").data(logsDTO).build())
                 .build();
        }
        catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ResponseDTO.builder().title("Erro ao listar logs").description(e.getMessage()).build())
                .build();
        }
        // List<Log> logsEntity = logDAO.listAll(Sort.descending("createdAt"));
        // List<LogDTO> logsDTO = new ArrayList<>();
        // logsEntity.forEach((log)->{logsDTO.add(log.toDTO());});
        // try {
        //     return Response.status(Response.Status.OK)
        //         .entity(ResponseDTO.builder().title("Logs listados com sucesso").data(logsDTO).build())
        //         .build();
        // } catch (Exception e){
        //     return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        //         .entity(ResponseDTO.builder().title("Erro ao listar logs").description(e.getMessage()).build())
        //         .build();
        // }
    }
}
