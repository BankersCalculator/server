package com.bankersCalculator.server.common.oauth.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationFailEntryPoint implements AuthenticationEntryPoint {

    private static final String EXCEPTION_ENTRY_POINT = "/api/v1/exception/entry-point";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendRedirect(EXCEPTION_ENTRY_POINT);
    }
}
