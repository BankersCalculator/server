package com.bankersCalculator.server.user.controller;

import com.bankersCalculator.server.user.test.KakaoUserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/login")
@RestController
public class UserController {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String client_id;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect_uri;

    private final UserService userService;

    @GetMapping
    public String loginPage() {
        String kakaoAuthorizeRedirectUri = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;
        return kakaoAuthorizeRedirectUri;
    }

    @GetMapping("/redirect")
    public KakaoUserInfoResponseDto loginRedirect(@RequestParam("code") String code) {

        userService.login(code);
        return null;
    }

}