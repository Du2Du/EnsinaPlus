package org.du2du.ensinaplus.filter;

import java.util.Objects;
import java.util.Set;

import org.du2du.ensinaplus.security.RequireRole;
import org.du2du.ensinaplus.utils.TokenUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.annotation.Priority;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    TokenUtils tokenUtils;

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (resourceInfo.getResourceMethod().isAnnotationPresent(PermitAll.class)) {
            return;
        }
        String authorizationHeader = !requestContext.getCookies().isEmpty() ?
         requestContext.getCookies().get("Authorization").getValue() : null;

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Token não fornecido ou inválido")
                    .build());
            return;
        }

        String token = authorizationHeader.substring("Bearer ".length()).trim();

        try {
            JsonWebToken jwt = tokenUtils.validateToken(token);
            if (Objects.isNull(jwt)) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Token inválido ou expirado")
                        .build());
            }
            this.validateRoles(jwt.getGroups(), requestContext);
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Erro ao validar token")
                    .build());
        }
    }

    private void validateRoles(Set<String> roles, ContainerRequestContext requestContext) {
        if (!resourceInfo.getResourceMethod().isAnnotationPresent(RequireRole.class))
            return;

        RequireRole requireRole = resourceInfo.getResourceMethod().getAnnotation(RequireRole.class);
        String[] requiredRoles = requireRole.value();

        for (String role : requiredRoles) {
            if (roles.contains(role)) {
                return;
            }
        }

        requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                .entity("Usuário não possui permissão para acessar este recurso")
                .build());
    }
}