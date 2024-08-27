package com.bankersCalculator.server.oauth.userInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class KakaoUserDetails implements OAuth2User {

    private final String providerId;
    private final List<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;


    @Override
    public String getName() {
        return providerId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
