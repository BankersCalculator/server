package com.myZipPlan.server.user.controller;

import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.oauth.token.TokenDto;
import com.myZipPlan.server.user.userService.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/guest")
@RequiredArgsConstructor
public class GuestApiController {

    private final GuestService guestService;

    // 게스트 로그인 (자동 등록)
    @GetMapping("/login")
    public ApiResponse<TokenDto> loginGuest() {
        TokenDto tokenDto = guestService.registerGuest();
        return ApiResponse.ok(tokenDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/pre-authorize")
    public ApiResponse<String> preAuthorize() {
        return ApiResponse.ok("USER 인증 완료");
    }
}
