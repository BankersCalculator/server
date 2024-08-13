package com.bankersCalculator.server.user.controller;

import com.bankersCalculator.server.common.api.ApiResponse;
import com.bankersCalculator.server.common.oauth.token.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    @GetMapping("/login/kakao")
    public ApiResponse<TokenDto> loginKakao(@RequestParam String accessToken,
                                            @RequestParam String refreshToken) {
        return ApiResponse.ok(TokenDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build());
    }
}
