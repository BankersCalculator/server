package com.myZipPlan.server.oauth.userInfo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class KakaoUserInfo {

    public static final String KAKAO_ACCOUNT = "kakao_account";
    public static final String EMAIL = "email";
    public static final String PROVIDER = "KAKAO";
    public static final String KAKAO_ID = "id";
    public static final String NICKNAME = "nickname";
    public static final String THUMBNAIL_IMAGE = "thumbnail_image";
    public static final String PROPERTIES = "properties";


    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getProvider() {
        return PROVIDER;
    }

    public String getProviderId() {
        Object id = attributes.get(KAKAO_ID);
        return id != null ? id.toString() : null;
    }

    public UserProfile getUserProfile() {
        Map<String, Object> allInfo = getObjectMap();
        String nickname = "";
        String email = "";
        String thumbnailImage = "";

        email = (String) allInfo.get(EMAIL);
        nickname = (String) allInfo.get(NICKNAME);
        thumbnailImage = (String) allInfo.get(THUMBNAIL_IMAGE);


        return UserProfile.builder()
            .email(email)
            .nickname(nickname)
            .thumbnailImage(thumbnailImage)
            .build();
    }


    private Map<String, Object> getObjectMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> result = new HashMap<>();

        // kakao_account 처리
        if (attributes.containsKey(KAKAO_ACCOUNT)) {
            Object kakaoAccount = attributes.get(KAKAO_ACCOUNT);
            result.putAll(objectMapper.convertValue(kakaoAccount, new TypeReference<Map<String, Object>>() {}));
        }

        // properties 처리
        if (attributes.containsKey(PROPERTIES)) {
            Object properties = attributes.get(PROPERTIES);
            result.putAll(objectMapper.convertValue(properties, new TypeReference<Map<String, Object>>() {}));
        }

        // 기타 최상위 속성들 추가
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            if (!entry.getKey().equals(KAKAO_ACCOUNT) && !entry.getKey().equals(PROPERTIES)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    public void logAllAttributes() {
        log.info("KakaoUserInfo attributes");
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            log.info("{}: {}", entry.getKey(), entry.getValue());
        }
        log.info("KakaoUserInfo attributes end");
    }
}
