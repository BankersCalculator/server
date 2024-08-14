package com.bankersCalculator.server.oauth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SecurityPathConfig {

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/error",
        "/favicon.ico",
        "/exception/**",
        "/login/oauth2/*",
        "/docs/**",
        "/api/v1/exception/**"
    );

    public String[] getPublicPaths() {
        return PUBLIC_PATHS.toArray(new String[0]);
    }

    public boolean isPublicPath(String path) {

        return PUBLIC_PATHS.stream().anyMatch(publicPath -> {
            if (publicPath.endsWith("/**")) {
                String basePath = publicPath.substring(0, publicPath.length() - 3);
                return path.startsWith(basePath);
            } else {
                return path.equals(publicPath);
            }
        });
    }
}