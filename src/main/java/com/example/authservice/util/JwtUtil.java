package com.example.authservice.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    @Value("${jwt.secret:VerySecretKey12345678901234567890}")
    private String secret;
    private static final long EXP_MS = 24*60*60*1000;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String subject, String role, List<Long> deptIds) {
        JwtBuilder b = Jwts.builder().setSubject(subject)
            .claim("role", role)
            .claim("departments", deptIds)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXP_MS));
        return b.signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
    }
}
