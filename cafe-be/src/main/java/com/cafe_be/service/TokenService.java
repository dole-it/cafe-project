package com.cafe_be.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long expirationMs;

    public TokenService(Environment env,
                        @Value("${app.jwt.expiration-ms:3600000}") long expirationMs) {
        String secret = env.getProperty("app.jwt.secret");
        if (secret == null || secret.isBlank()) {
            // fallback to environment variable if provided
            secret = env.getProperty("APP_JWT_SECRET");
        }
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT secret not configured. Set 'app.jwt.secret' in application.properties or environment variable APP_JWT_SECRET");
        }

        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
        this.expirationMs = expirationMs;
    }

    public String createToken(com.cafe_be.model.User user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        com.auth0.jwt.JWTCreator.Builder builder = JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(exp);

        if (user.getEmail() != null) builder.withClaim("email", user.getEmail());
        if (user.getFullName() != null) builder.withClaim("fullName", user.getFullName());
        if (user.getId() != null) builder.withClaim("id", user.getId());

        // roles as list
        java.util.List<String> roles = user.getRole() == null ? java.util.List.of() : java.util.List.of(user.getRole().name());
        builder.withClaim("roles", roles);

        return builder.sign(algorithm);
    }

    public DecodedJWT verify(String token) {
        return verifier.verify(token);
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT jwt = verify(token);
        return jwt.getSubject();
    }
}
