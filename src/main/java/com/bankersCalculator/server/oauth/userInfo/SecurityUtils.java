package com.bankersCalculator.server.oauth.userInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SecurityUtils {

    public static String getProviderId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Security Context에 인증 정보가 없습니다.");
        }
        User principal = (User) authentication.getPrincipal();
        return  principal.getUsername();
    }
}
