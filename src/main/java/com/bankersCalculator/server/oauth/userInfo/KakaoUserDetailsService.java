package com.bankersCalculator.server.oauth.userInfo;

import com.bankersCalculator.server.common.enums.RoleType;
import com.bankersCalculator.server.user.domain.User;
import com.bankersCalculator.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;


@RequiredArgsConstructor
@Service
@Slf4j
public class KakaoUserDetailsService extends DefaultOAuth2UserService {

    private static final String PROVIDER = "KAKAO";

    private final UserRepository userRepository;


    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.debug("사람살려...");
        OAuth2User oAuth2User = super.loadUser(userRequest);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        String email = kakaoUserInfo.getEmail();



        log.debug("email" + email);

        User user = userRepository.findByOauthProviderAndEmail(PROVIDER, email)
            .orElseGet(() -> userRepository.save(
                User.builder()
                    .oauthProvider(PROVIDER)
                    .email(email)
                    .roleType(RoleType.USER)
                    .build()
            ));

        log.debug("user" + user.toString());


        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRoleType().getCode());
        log.debug("authority" + authority);


        return new KakaoUserDetails(String.valueOf(user.getEmail()),
            Collections.singletonList(authority),
            oAuth2User.getAttributes());
    }
}
