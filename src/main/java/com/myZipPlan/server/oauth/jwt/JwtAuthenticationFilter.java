package com.myZipPlan.server.oauth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.common.enums.RoleType;
import com.myZipPlan.server.common.exception.customException.AuthException;
import com.myZipPlan.server.oauth.config.SecurityPathConfig;
import com.myZipPlan.server.oauth.token.TokenDto;
import com.myZipPlan.server.oauth.token.TokenProvider;
import com.myZipPlan.server.oauth.token.TokenValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String ACCESS_HEADER = "AccessToken";
    private static final String REFRESH_HEADER = "RefreshToken";

    private final TokenValidator tokenValidator;
    private final TokenProvider tokenProvider;

    private final SecurityPathConfig securityPathConfig;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. Public Path
        if (securityPathConfig.isPublicPath(request.getRequestURI())) {
            handlePublicPath(request, response, filterChain);
            return;
        }
        // 2. 일반 처리
        handleJwtAuthentication(request, response, filterChain);
    }

    private void handlePublicPath(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String accessToken = request.getHeader(ACCESS_HEADER);
        if (isTokenValidAndNotExpired(accessToken)) {
            SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(accessToken));
        }
        filterChain.doFilter(request, response);
    }

    private void handleJwtAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String accessToken = request.getHeader(ACCESS_HEADER);
        String refreshToken = request.getHeader(REFRESH_HEADER);

        log.warn("AuthenticationFilter hello0");

        log.warn("accessToken : {}", accessToken);

        if (isTokenValidAndNotExpired(accessToken)) {
            log.warn("AuthenticationFilter hello1");
            SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(accessToken));
            filterChain.doFilter(request, response);
            return;
        }

        if (isTokenValidButExpired(accessToken)) {
            log.warn("AuthenticationFilter hello2");
            if (isTokenValidAndNotExpired(refreshToken)) {
                log.warn("AuthenticationFilter hello3");
                TokenDto newTokenDto = tokenProvider.reissueAccessToken(refreshToken);
                SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(newTokenDto.getAccessToken()));
                redirectReissueURI(request, response, newTokenDto);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isTokenValidAndNotExpired(String accessToken) {
        return accessToken != null
            && tokenValidator.validateExpire(accessToken)
            && tokenValidator.validateToken(accessToken);
    }

    private boolean isTokenValidButExpired(String accessToken) {
        return !tokenValidator.validateExpire(accessToken) && tokenValidator.validateToken(accessToken);
    }

    private void redirectReissueURI(HttpServletRequest request, HttpServletResponse response, TokenDto newTokenDto) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("accessToken", newTokenDto.getAccessToken());
        session.setAttribute("refreshToken", newTokenDto.getRefreshToken());
        response.sendRedirect("/login/oauth2/kakao/reissue");
    }
}
