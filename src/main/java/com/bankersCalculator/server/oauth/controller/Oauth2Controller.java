package com.bankersCalculator.server.oauth.controller;

import com.bankersCalculator.server.common.api.ApiResponse;
import com.bankersCalculator.server.oauth.token.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/login/oauth2/kakao")
@RequiredArgsConstructor
@RestController
public class Oauth2Controller {

    @GetMapping
    public ApiResponse<TokenDto> loginKakao(@RequestParam String accessToken,
                                            @RequestParam String refreshToken) {
        return ApiResponse.ok(TokenDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build());
    }

    @GetMapping("/reissue")
    public ApiResponse<TokenDto> reissueToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String accessToken = (String) session.getAttribute("accessToken");
        String refreshToken = (String) session.getAttribute("refreshToken");

        return ApiResponse.ok(TokenDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build());
    }
}
