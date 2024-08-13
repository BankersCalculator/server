package com.bankersCalculator.server.common.oauth.token;

import com.bankersCalculator.server.common.oauth.user.KakaoUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class TokenProvider {

    private static final String AUTH_KEY = "AUTHORITY";
    private static final String AUTH_PROVIDER = "PROVIDER";
    private static final String AUTH_ID = "ID";

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenValiditySeconds;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValiditySeconds;

    private Key jwtKey;

    @PostConstruct
    public void intiKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.jwtKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createToken(String provider, String providerId, String role) {
        long now = new Date().getTime();

        Date accessValidity = new Date(now + accessTokenValiditySeconds);
        Date refreshValidity = new Date(now + refreshTokenValiditySeconds);

        String accessToken = Jwts.builder()
            .addClaims(Map.of(AUTH_PROVIDER, provider))
            .addClaims(Map.of(AUTH_ID, providerId))
            .addClaims(Map.of(AUTH_KEY, role))
            .signWith(jwtKey, SignatureAlgorithm.HS256)
            .setExpiration(accessValidity)
            .compact();

        String refreshToken = Jwts.builder()
            .addClaims(Map.of(AUTH_PROVIDER, provider))
            .addClaims(Map.of(AUTH_ID, providerId))
            .addClaims(Map.of(AUTH_KEY, role))
            .signWith(jwtKey, SignatureAlgorithm.HS256)
            .setExpiration(refreshValidity)
            .compact();

        return TokenDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(jwtKey)
            .build()
            .parseClaimsJws(token)
            .getBody();

        List<String> authorities = Arrays.asList(claims.get(AUTH_KEY)
            .toString()
            .split(","));

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
            .map(auth -> new SimpleGrantedAuthority(auth))
            .toList();

        KakaoUserDetails principal = new KakaoUserDetails((String) claims.get(AUTH_ID), simpleGrantedAuthorities, Map.of());

        return new UsernamePasswordAuthenticationToken(principal, token, simpleGrantedAuthorities);
    }
}
