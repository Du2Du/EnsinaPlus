package org.du2du.ensinaplus.utils;

import java.util.Set;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.NewCookie;

@Singleton
public class SessionUtils {

  @Inject
  TokenUtils tokenUtils;

  public NewCookie createCookie(String role) {
    String token = tokenUtils.generate(Set.of(role));
    return new NewCookie.Builder("Authorization")
        .value("Bearer " + token)
        .path("/")
        .maxAge(86400)
        .httpOnly(true)
        .secure(true)
        .build();
  }
}
