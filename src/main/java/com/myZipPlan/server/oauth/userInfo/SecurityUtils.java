package com.myZipPlan.server.oauth.userInfo;

import com.myZipPlan.server.oauth.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SecurityUtils {

    private final TokenProvider tokenProvider;

    public static String getProviderId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Security Context에 인증 정보가 없습니다.");
        }
        OAuth2User principal = (KakaoUserDetails) authentication.getPrincipal();
        return principal.getName();
    }

    public static String getGuestProviderId(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalStateException("Security Context에 인증 정보가 없습니다.");
        }
        OAuth2User principal = (KakaoUserDetails) authentication.getPrincipal();
        return principal.getName();
    }
}
