package com.bankersCalculator.server.oauth.userInfo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class KakaoUserInfo {

    public static final String KAKAO_ACCOUNT = "kakao_account";
    public static final String EMAIL = "email";
    public static final String PROVIDER = "KAKAO";
    public static final String KAKAO_ID = "id";

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getEmail() {
        Map<String, Object> account = getObjectMap();

        return (String) account.get(EMAIL);
    }

    public String getProvider() {
        return PROVIDER;
    }



    private Map<String, Object> getObjectMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};

        Object kakaoAccount = attributes.get(KAKAO_ACCOUNT);
        Map<String, Object> account = objectMapper.convertValue(kakaoAccount, typeReference);
        return account;
    }
}
