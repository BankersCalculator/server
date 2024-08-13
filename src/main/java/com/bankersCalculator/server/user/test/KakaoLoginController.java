package com.bankersCalculator.server.user.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class KakaoLoginController {

    private final KakaoService kakaoService;

    @GetMapping("/oauth2/callback/kakao")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {

        log.info("hellloo~~~~~");


        // User 로그인, 또는 회원가입 로직 추가
        return new ResponseEntity<>(HttpStatus.OK);
    }
}