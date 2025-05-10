package org.du2du.ensinaplus.utils;

import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class TokenUtils {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @Inject
    JWTParser parser;

    public String generate(Set<String> roles) {
        return Jwt.issuer(issuer)
            .subject("ensina-plus")
            .expiresAt(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
            .groups(roles).sign();
    }

    public JsonWebToken validateToken(String token) {
        try {
            return parser.parse(token);
        } catch (ParseException e) {
            return null;
        }
    }
}
