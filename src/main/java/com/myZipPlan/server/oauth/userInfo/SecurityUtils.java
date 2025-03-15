package com.myZipPlan.server.oauth.userInfo;

import com.myZipPlan.server.common.enums.RoleType;
import com.myZipPlan.server.oauth.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;

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

    public static String getGuestProviderIdForTransfer(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalStateException("Security Context에 인증 정보가 없습니다.");
        }

        OAuth2User principal = (KakaoUserDetails) authentication.getPrincipal();
        return principal.getName();
    }

    public static RoleType getRoleType() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities().isEmpty()) {
            throw new IllegalStateException("Security Context에 인증 정보가 없습니다.");
        }
        return authentication.getAuthorities().stream()
            .map(grantedAuthority -> RoleType.of(grantedAuthority.getAuthority()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("유효한 역할이 없습니다."));
    }
}
