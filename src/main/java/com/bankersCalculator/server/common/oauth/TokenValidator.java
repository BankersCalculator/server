package com.bankersCalculator.server.common.oauth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class TokenValidator {

    private static final String AUTH_KEY = "AUTHORITY";
    private static final String AUTH_PROVIDER = "PROVIDER";
    private static final String AUTH_ID = "ID";

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-validity-in-seconds")
    private long accessTokenValiditySeconds;

    @Value("${jwt.refresh-token-validity-in-seconds")
    private long refreshTokenValiditySeconds;

    private Key jwtKey;

    @PostConstruct
    public void intiKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.jwtKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateExpire(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }
}
