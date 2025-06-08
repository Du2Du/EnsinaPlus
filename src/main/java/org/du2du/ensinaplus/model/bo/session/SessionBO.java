package org.du2du.ensinaplus.model.bo.session;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.du2du.ensinaplus.model.dto.UserDTO;
import org.du2du.ensinaplus.model.entity.session.Session;
import org.du2du.ensinaplus.service.RedisSessionService;
import org.du2du.ensinaplus.utils.TokenUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.NewCookie;

@ApplicationScoped
@Named
public class SessionBO implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String SESSION_COOKIE_NAME = "ensina-plus-session";
    
    @Inject
    TokenUtils tokenUtils;
    
    @Inject
    RedisSessionService redisSessionService;


    public NewCookie createSession(UserDTO user) {
        UUID sessionId = UUID.randomUUID();
        
        Session session = Session.builder()
            .data(user)
            .createdAt(LocalDateTime.now())
            .uuid(sessionId)
            .build();
            
        redisSessionService.saveSession(session);
        
        return new NewCookie.Builder(SESSION_COOKIE_NAME)
            .value(sessionId.toString())
            .path("/")
            .maxAge(86400)
            .httpOnly(true)
            .secure(true)
            .build();
    }


    public Session getSession(Cookie sessionCookie) {
        if (sessionCookie == null) {
            return null;
        }
        
        try {
            UUID sessionId = UUID.fromString(sessionCookie.getValue());
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

    public void updateSession(UserDTO dto, HttpHeaders headers) {
        Session session = getSession(headers.getCookies().get(SESSION_COOKIE_NAME));
        if (Objects.isNull(session)) return;
        session.setData(dto);
        redisSessionService.saveSession(session);
    }

    public NewCookie deleteSession(Cookie sessionCookie) {
        if (sessionCookie != null) {
            try {
                UUID sessionId = UUID.fromString(sessionCookie.getValue());
                redisSessionService.deleteSession(sessionId);
            } catch (IllegalArgumentException e) {
            }
        }
        
        return new NewCookie.Builder(SESSION_COOKIE_NAME)
            .value("")
            .path("/")
            .maxAge(0)
            .httpOnly(true)
            .secure(true)
            .build();
    }

    public NewCookie createAuthCookie(String role) {
        String token = tokenUtils.generate(Set.of(role));
        return new NewCookie.Builder("ensina-plus-auth")
            .value("Bearer " + token)
            .path("/")
            .maxAge(86400)
            .httpOnly(true)
            .secure(true)
            .build();
    }

}
