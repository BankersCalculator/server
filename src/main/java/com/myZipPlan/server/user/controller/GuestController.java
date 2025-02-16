package com.myZipPlan.server.user.controller;

import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.oauth.token.TokenDto;
import com.myZipPlan.server.user.userService.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/guest")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    // 게스트 로그인 (자동 등록)
    @GetMapping("/login")
    public ApiResponse<TokenDto> loginGuest() {
        TokenDto tokenDto = guestService.registerGuest();
        return ApiResponse.ok(tokenDto);
    }
}
