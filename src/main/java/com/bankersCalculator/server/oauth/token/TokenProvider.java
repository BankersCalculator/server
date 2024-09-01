package com.bankersCalculator.server.oauth.token;

import com.bankersCalculator.server.common.exception.customException.AuthException;
import com.bankersCalculator.server.oauth.repository.RefreshTokenRedisRepository;
import com.bankersCalculator.server.oauth.repository.TempUserTokenRedisRepository;
import com.bankersCalculator.server.oauth.userInfo.KakaoUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String AUTH_KEY = "AUTHORITY";
    private static final String AUTH_ID = "PROVIDER_ID";

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-validity-in-seconds}")
    private int accessTokenValiditySeconds;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private int refreshTokenValiditySeconds;


    private Key jwtKey;

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final TempUserTokenRedisRepository tempUserTokenRedisRepository;

    @PostConstruct
    public void intiKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.jwtKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createToken(String providerId, String role) {
        long now = new Date().getTime();

        long accessTokenValidityMilliSeconds = accessTokenValiditySeconds * 1000L;
        long refreshTokenValidityMilliSeconds = refreshTokenValiditySeconds * 1000L;


        Date accessValidity = new Date(now + accessTokenValidityMilliSeconds);
        Date refreshValidity = new Date(now + refreshTokenValidityMilliSeconds);

        String accessToken = Jwts.builder()
            .addClaims(Map.of(AUTH_ID, providerId))
            .addClaims(Map.of(AUTH_KEY, role))
            .signWith(jwtKey, SignatureAlgorithm.HS256)
            .setExpiration(accessValidity)
            .compact();

        String refreshToken = Jwts.builder()
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

    @Transactional
    public TokenDto reissueAccessToken(String refreshToken) {
        RefreshToken findToken = refreshTokenRedisRepository.findByRefreshToken(refreshToken);


        TokenDto newTokenDto = createToken(findToken.getProviderId(), findToken.getAuthority());

        refreshTokenRedisRepository.save(RefreshToken.builder()
            .providerId(findToken.getProviderId())
            .authorities(findToken.getAuthorities())
            .refreshToken(newTokenDto.getRefreshToken())
            .build());
        return newTokenDto;
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

    public Authentication getTempUserAuthentication(String tempUserId) {

        TempUserToken byTempUserId = tempUserTokenRedisRepository.findByTempUserId(tempUserId);
        if (byTempUserId != null) {
            throw new AuthException("이미 1회 산출한 TempUserId 입니다.");
        }

        tempUserTokenRedisRepository.save(TempUserToken.builder()
            .tempUserId(tempUserId)
            .build());

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_TEMP_USER"));


        KakaoUserDetails principal = new KakaoUserDetails(tempUserId, simpleGrantedAuthorities, Map.of());

        return new UsernamePasswordAuthenticationToken(principal, tempUserId, simpleGrantedAuthorities);
    }
}
