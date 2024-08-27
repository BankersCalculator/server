package com.bankersCalculator.server.oauth.jwt;

import com.bankersCalculator.server.oauth.config.SecurityPathConfig;
import com.bankersCalculator.server.oauth.token.TokenDto;
import com.bankersCalculator.server.oauth.token.TokenProvider;
import com.bankersCalculator.server.oauth.token.TokenValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


        if (securityPathConfig.isPublicPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }





        String accessToken = request.getHeader(ACCESS_HEADER);

        if (tokenValidator.validateExpire(accessToken) && tokenValidator.validateToken(accessToken)) {
            SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(accessToken));
        }

        if (!tokenValidator.validateExpire(accessToken) && tokenValidator.validateToken(accessToken)) {
            String refreshToken = request.getHeader(REFRESH_HEADER);
            if (tokenValidator.validateExpire(refreshToken) && tokenValidator.validateToken(refreshToken)){

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
}
