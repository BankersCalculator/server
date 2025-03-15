package com.myZipPlan.server.user.controller;

import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.common.enums.RoleType;
import com.myZipPlan.server.oauth.token.TokenDto;
import com.myZipPlan.server.user.dto.GuestToUserTransferRequest;
import com.myZipPlan.server.user.userService.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.antlr.runtime.Token;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/transfer")
    public ApiResponse<TokenDto> transferTempUserToLoginUser(@RequestBody GuestToUserTransferRequest request,
                                                             HttpServletRequest httpServletRequest) {
        TokenDto accessToken = extractAccessToken(httpServletRequest);
        TokenDto tokenDto = userService.transferGuestToUser(request.getGuestToken(), accessToken);

        return ApiResponse.ok(tokenDto);
    }

    @PostMapping("/logout")
    public void logout() {
        userService.logout();

    }
    //회원탈퇴
    @PostMapping("/withdraw")
    public void withdraw() {
        userService.withdraw();
    }

    private TokenDto extractAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader("AccessToken");
        String refreshToken = request.getHeader("RefreshToken");
        return TokenDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .roleType(RoleType.USER)
            .build();
    }
}
