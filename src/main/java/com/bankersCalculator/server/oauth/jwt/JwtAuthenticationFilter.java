package com.bankersCalculator.server.oauth.jwt;

import com.bankersCalculator.server.oauth.config.SecurityPathConfig;
import com.bankersCalculator.server.oauth.token.TokenProvider;
import com.bankersCalculator.server.oauth.token.TokenValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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



        //TODO : 수정요망
//        if (securityPathConfig.isPublicPath(request.getRequestURI())) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        String accessToken = request.getHeader(ACCESS_HEADER);



        if (tokenValidator.validateExpire(accessToken) && tokenValidator.validateToken(accessToken)) {
            SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(accessToken));
        }

        filterChain.doFilter(request, response);

    }
}
