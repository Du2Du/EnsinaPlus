package org.du2du.ensinaplus.model.bo.session;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.du2du.ensinaplus.model.dto.UserDTO;
import org.du2du.ensinaplus.model.entity.session.Session;
import org.du2du.ensinaplus.service.RedisSessionService;
import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named
public class SessionBO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Inject
    JsonWebToken jwt;

    @Inject
    RedisSessionService redisSessionService;

    public UUID createSession(UserDTO user) {
        UUID sessionId = UUID.randomUUID();

        Session session = Session.builder()
                .data(user)
                .createdAt(LocalDateTime.now())
                .uuid(sessionId)
                .build();

        redisSessionService.saveSession(session);

        return sessionId;
    }

    public Session getSession() {
        String sessaoUUID = jwt.claim("sessaoUUID").get().toString();
        if (sessaoUUID == null) {
            return null;
        }
        try {
            UUID sessionId = UUID.fromString(sessaoUUID);
            Optional<Session> sessionOpt = redisSessionService.getSession(sessionId);
            if (sessionOpt.isPresent()) {
                redisSessionService.updateSessionExpiry(sessionId);
                return sessionOpt.get();
            }
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void updateSession(UserDTO dto) {
        Session session = getSession();
        if (Objects.isNull(session))
            return;
        session.setData(dto);
        redisSessionService.saveSession(session);
    }

    public void deleteSession() {
        String sessaoUUID = jwt.claim("sessaoUUID").get().toString();
        if (sessaoUUID != null) {
            try {
                UUID sessionId = UUID.fromString(sessaoUUID);
                redisSessionService.deleteSession(sessionId);
            } catch (IllegalArgumentException e) {
            }
        }
    }

}
