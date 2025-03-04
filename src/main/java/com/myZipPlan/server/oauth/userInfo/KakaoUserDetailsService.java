package com.myZipPlan.server.oauth.userInfo;

import com.myZipPlan.server.common.enums.RoleType;
import com.myZipPlan.server.community.service.S3Service;
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
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Slf4j
public class KakaoUserDetailsService extends DefaultOAuth2UserService {

    private static final String PROVIDER = "KAKAO";

    private final S3Service s3Service;
    private static final String USER_PROFILE_DIR = "user-profile/"; // 유저 프로필 전용 디렉토리
    private static final int ANIMAL_PROFILE_IMAGE_COUNT = 10;


    private final UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());

        UserProfile userProfile = kakaoUserInfo.getUserProfile();
        String kakaoNickName =userProfile.getNickname();
        String email = userProfile.getEmail();
        String thumbnailImage = userProfile.getThumbnailImage();
        String providerId = kakaoUserInfo.getProviderId();
        String animalProfileImageUrl = generateRandomAnimalProfileImageUrl();

        User user = userRepository.findByProviderAndProviderId(PROVIDER, providerId)
            .orElseGet(() -> userRepository.save(
                User.create("KAKAO", providerId, kakaoNickName, email, thumbnailImage, RoleType.USER, animalProfileImageUrl)
            ));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRoleType().getCode());

        return new KakaoUserDetails(String.valueOf(user.getEmail()),
            Collections.singletonList(authority),
            oAuth2User.getAttributes());
    }

    public String generateRandomAnimalProfileImageUrl() {
        int randomIndex = new Random().nextInt(ANIMAL_PROFILE_IMAGE_COUNT) + 1;
        String animalProfileImgUrlTemplate = s3Service.getS3DirectoryUrl(USER_PROFILE_DIR) + "comunity-profile-%02d.png";
        return String.format(animalProfileImgUrlTemplate, randomIndex);
    }
}
