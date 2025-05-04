package org.du2du.ensinaplus.utils;

import java.util.Set;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Singleton;

@Singleton
public class GenerateToken {

  public static String generate(Set<String> roles, String email) {
    return Jwt.issuer("ensina-plus")
        .subject("ensina-plus")
        .expiresAt(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
        .groups(roles).sign();
  }
}
