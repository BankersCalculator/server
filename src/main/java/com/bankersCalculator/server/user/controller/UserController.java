package com.bankersCalculator.server.user.controller;

import com.bankersCalculator.server.common.api.ApiResponse;
import com.bankersCalculator.server.oauth.token.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RequiredArgsConstructor
@RestController
public class UserController {

    @GetMapping("/api/user/login/kakao")
    public ApiResponse<TokenDto> loginKakao(@RequestParam String code,
                                            @RequestParam String refreshToken) {
        return ApiResponse.ok(TokenDto.builder()
            .accessToken(code)
            .refreshToken(refreshToken)
            .build());
    }
}
