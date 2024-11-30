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
import java.util.List;
import java.util.Random;


@RequiredArgsConstructor
@Service
@Slf4j
public class KakaoUserDetailsService extends DefaultOAuth2UserService {

    private static final String PROVIDER = "KAKAO";

    private static final String ANIMAL_PROFILE_IMAGE_URL_TEMPLATE = "https://myzipplan-service-storage-dev.s3.ap-northeast-2.amazonaws.com/user-profile/comunity-profile-%02d.png";
    private static final int ANIMAL_PROFILE_IMAGE_COUNT = 12;
    private static final List<String> ANIMAL_USERNAME_ADJECTIVES = List.of("포효하는", "눈썰매타는", "재테크천재", "독서하는", "춤추는", "밈잘알", "행복한", "느긋한", "용감한", "우아한", "청약노리는", "건물주꿈꾸는", "임대사업하는", "월세탈출하는", "아파트분양받는", "분산투자왕", "저축왕", "안전자산추구하는", "거시경제분석하는");
    private static final List<String> ANIMAL_USERNAME_ANIMALS = List.of("쿼카", "코끼리", "비버", "개구리", "호랑이", "펭귄", "다람쥐", "고양이", "여우", "사슴", "알파카", "사막여우", "바다표범", "돌고래", "치타", "늑대","미어캣" );


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
        String animalUserName = generateRandomAnimalUsername();

        User user = userRepository.findByOauthProviderAndOauthProviderId(PROVIDER, providerId)
            .orElseGet(() -> userRepository.save(
                User.create("KAKAO", providerId, kakaoNickName, email, thumbnailImage, RoleType.USER, animalProfileImageUrl, animalUserName )
            ));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRoleType().getCode());

        return new KakaoUserDetails(String.valueOf(user.getEmail()),
            Collections.singletonList(authority),
            oAuth2User.getAttributes());
    }

    public String generateRandomAnimalProfileImageUrl() {
        int randomIndex = new Random().nextInt(ANIMAL_PROFILE_IMAGE_COUNT) + 1;
        return String.format(ANIMAL_PROFILE_IMAGE_URL_TEMPLATE, randomIndex);
    }

    public String generateRandomAnimalUsername() {
        Random random = new Random();
        String adjective = ANIMAL_USERNAME_ADJECTIVES.get(random.nextInt(ANIMAL_USERNAME_ADJECTIVES.size()));
        String animal = ANIMAL_USERNAME_ANIMALS.get(random.nextInt(ANIMAL_USERNAME_ANIMALS.size()));
        return adjective + " " + animal;
    }
}
