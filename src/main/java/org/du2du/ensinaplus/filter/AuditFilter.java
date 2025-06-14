package org.du2du.ensinaplus.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import org.du2du.ensinaplus.model.bo.impl.LogBO;
import org.du2du.ensinaplus.model.bo.session.SessionBO;
import org.du2du.ensinaplus.model.dto.LogDTO;
import org.du2du.ensinaplus.security.ActionDescription;
import org.du2du.ensinaplus.security.NotRequiredAudit;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AuditFilter implements ContainerRequestFilter {

    @Inject
    LogBO logBO;

    @Inject
    SessionBO sessionBO;

    @Inject
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (resourceInfo.getResourceMethod().isAnnotationPresent(NotRequiredAudit.class)
                || sessionBO.getSession() == null) {
            return;
        }
        LogDTO logDTO = LogDTO.builder().uuid(null).method(requestContext.getMethod())
                .url(requestContext.getUriInfo().getAbsolutePath().toString())
                .uuidUser(
                        sessionBO.getUserDTO().getUuid())
                .nameUser(
                        sessionBO.getUserDTO().getName())
                .description(
                        Objects.isNull(resourceInfo.getResourceMethod().getAnnotation(ActionDescription.class)) ? ""
                                : resourceInfo.getResourceMethod().getAnnotation(ActionDescription.class).value()[0])
                .createdAt(LocalDateTime.now()).build();
        logBO.createLog(logDTO);
    }
}
