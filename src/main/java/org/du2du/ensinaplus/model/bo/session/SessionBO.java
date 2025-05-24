package org.du2du.ensinaplus.model.bo.session;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.du2du.ensinaplus.model.dto.UserDTO;
import org.du2du.ensinaplus.model.entity.session.Session;
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

    private final Map<UUID, Session> sessionMap = new ConcurrentHashMap<>();
    
    @Inject
    TokenUtils tokenUtils;

    /**
     * Cria uma nova sessão para o usuário e retorna o cookie de sessão
     */
    public NewCookie createSession(UserDTO user) {
        UUID sessionId = UUID.randomUUID();
        
        Session session = Session.builder()
            .data(user)
            .createdAt(LocalDateTime.now())
            .uuid(sessionId)
            .build();
            
        sessionMap.put(sessionId, session);
        
        return new NewCookie.Builder(SESSION_COOKIE_NAME)
            .value(sessionId.toString())
            .path("/")
            .maxAge(86400)
            .httpOnly(true)
            .secure(true)
            .build();
    }

    /**
     * Recupera a sessão do usuário a partir do cookie de sessão
     */
    public Session getSession(HttpHeaders headers) {
        Cookie sessionCookie = headers.getCookies().get(SESSION_COOKIE_NAME);
        if (sessionCookie == null) {
            return null;
        }
        
        try {
            UUID sessionId = UUID.fromString(sessionCookie.getValue());
            return sessionMap.get(sessionId);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    public void updateSession(UserDTO dto, HttpHeaders headers){
        Session session = getSession(headers);
        if (Objects.isNull(session))return;
        session.setData(dto);
        sessionMap.put(session.getUuid(), session);
    }

    public NewCookie deleteSession(HttpHeaders headers) {
        Cookie sessionCookie = headers.getCookies().get(SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            try {
                UUID sessionId = UUID.fromString(sessionCookie.getValue());
                sessionMap.remove(sessionId);
            } catch (IllegalArgumentException e) {
                // Ignora erro de parsing
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
