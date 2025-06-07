package org.du2du.ensinaplus.filter;

import java.io.IOException;
import java.time.LocalDateTime;

import org.du2du.ensinaplus.model.bo.impl.LogBO;
import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dto.LogDTO;
import org.du2du.ensinaplus.security.NotRequiredAudit;


import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AuditFilter implements ContainerRequestFilter{

    @Inject
    LogBO logBO;

    @Inject
    SessionBO sessionBO;

    @Inject
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
         if (resourceInfo.getResourceMethod().isAnnotationPresent(NotRequiredAudit.class)) {
            return;
        }
        LogDTO logDTO = new LogDTO(null, 
            requestContext.getMethod(), 
            requestContext.getUriInfo().getAbsolutePath().toString(), 
            sessionBO.getSession(requestContext.getCookies().get(("ensina-plus-session"))).getData().getUuid(), 
            "teste", 
            LocalDateTime.now());

        logBO.createLog(logDTO);
    }
}
