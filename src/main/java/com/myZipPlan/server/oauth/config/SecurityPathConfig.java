package com.myZipPlan.server.oauth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class SecurityPathConfig {

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/error",
        "/favicon.ico",
        "/exception/**",
        "/login/oauth2/*",
        "/login/oauth2/kakao/reissue",
        "/docs/**",
        "/api/v1/exception/**",

        "/api/v1/dsrCalc",
        "/api/v1/dtiCalc",
        "/api/v1/ltvCalc",
        "/api/v1/repaymentCalc",
        "/api/v1/housing-info/**",
        "/api/v1/addressSearch",
        "/api/v1/loanAdvice/simple",
        "/api/v1/rentTransactionInquiry",
        "/api/v1/rentTransaction",
        "/api/v1/housingTypeAndExclusiveArea",
        "/api/v1/housingInfo",
        "/api/v1/post",
        "/api/v1/post/sorted",
        "api/v1/webhook/github-actions/health-check",

        "/h2-console/**"

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