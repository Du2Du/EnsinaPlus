package org.du2du.ensinaplus.model.bo.session;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.du2du.ensinaplus.model.dto.UserDTO;
import org.du2du.ensinaplus.model.entity.session.Session;
import org.du2du.ensinaplus.utils.TokenUtils;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.NewCookie;
import lombok.Getter;
import lombok.Setter;

@Singleton
@Getter
@Setter
public class SessionBO {

    private Session session;
    
    public void createSession(UserDTO user) {
        this.session = Session.builder()
            .data(user)
            .createdAt(LocalDateTime.now())
            .uuid(UUID.randomUUID())
            .build();
    }

    public void deleteSession() {
        this.session = null;
    }

    
  @Inject
  TokenUtils tokenUtils;

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
