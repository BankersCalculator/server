package com.bankersCalculator.server.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class SecurityPathConfig {

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/favicon.ico",
        "/error",
        "/docs/**",
        "/h2-console/**",
        "/h2-console"
    );

    public List<String> getPublicPaths() {
        return PUBLIC_PATHS;
    }

    public boolean isPublicPath(String path) {

        return PUBLIC_PATHS.stream().anyMatch(publicPath -> {
            if (publicPath.endsWith("/**")) {
                String basePath = publicPath.substring(0, publicPath.length() - 2);
                return path.startsWith(basePath);
            } else {
                return path.equals(publicPath);
            }
        });
    }
}