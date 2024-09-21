package com.myZipPlan.server.oauth.userInfo;

import com.myZipPlan.server.common.enums.RoleType;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
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

        OAuth2User oAuth2User = super.loadUser(userRequest);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());

        UserProfile userProfile = kakaoUserInfo.getUserProfile();
        String nickname =userProfile.getNickname();
        String email = userProfile.getEmail();
        String thumbnailImage = userProfile.getThumbnailImage();
        String providerId = kakaoUserInfo.getProviderId();

        User user = userRepository.findByOauthProviderAndOauthProviderId(PROVIDER, providerId)
            .orElseGet(() -> userRepository.save(
                User.create("KAKAO", providerId, nickname, email, thumbnailImage, RoleType.USER)
            ));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRoleType().getCode());

        return new KakaoUserDetails(String.valueOf(user.getEmail()),
            Collections.singletonList(authority),
            oAuth2User.getAttributes());
    }
}
