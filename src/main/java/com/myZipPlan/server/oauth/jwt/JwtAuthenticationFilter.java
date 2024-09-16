package com.myZipPlan.server.oauth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myZipPlan.server.common.api.ApiResponse;
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
    private static final String TEMP_USER_HEADER = "TempUserId";

    private final TokenValidator tokenValidator;
    private final TokenProvider tokenProvider;

    private final SecurityPathConfig securityPathConfig;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        // 토큰이 필요없는 URI
        if (securityPathConfig.isPublicPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1회성 유저 패스 로직
        String TempUserIdToken = request.getHeader(TEMP_USER_HEADER);
        if (request.getRequestURI().equals("/api/v1/loanAdvice")
            && TempUserIdToken != null) {

            try {
                SecurityContextHolder.getContext().setAuthentication(tokenProvider.getTempUserAuthentication(TempUserIdToken));
            } catch (AuthException e) {
                sendErrorResponse(response, e.getMessage());
            }

            filterChain.doFilter(request, response);
            return;
        }


        String accessToken = request.getHeader(ACCESS_HEADER);

        if (tokenValidator.validateExpire(accessToken) && tokenValidator.validateToken(accessToken)) {
            SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(accessToken));
        }

        if (!tokenValidator.validateExpire(accessToken) && tokenValidator.validateToken(accessToken)) {
            String refreshToken = request.getHeader(REFRESH_HEADER);
            if (tokenValidator.validateExpire(refreshToken) && tokenValidator.validateToken(refreshToken)) {

                TokenDto newTokenDto = tokenProvider.reissueAccessToken(refreshToken);

                SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(newTokenDto.getAccessToken()));
                redirectReissueURI(request, response, newTokenDto);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void redirectReissueURI(HttpServletRequest request, HttpServletResponse response, TokenDto newTokenDto) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("accessToken", newTokenDto.getAccessToken());
        session.setAttribute("refreshToken", newTokenDto.getRefreshToken());
        response.sendRedirect("/login/oauth2/kakao/reissue");
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Object> apiResponse = ApiResponse.of(
            HttpStatus.UNAUTHORIZED,
            message,
            null
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
