package com.mape.portfolio_app.util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;

import java.util.Date;
import java.util.Map;

@Component
public class JsonWebTokenUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @SuppressWarnings("deprecation")
    public String generateToken(int userId, String role) {
        long expirationMillis = 3600000; // 1 hour

        SecretKey key = getSecretKey();

        Map<String, Object> claims = Map.of(
                "role", role
        );

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId)) 
                .setIssuedAt(new Date()) 
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis)) 
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
}
