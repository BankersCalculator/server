package com.myZipPlan.server.user.controller;

import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.user.dto.TempUserTransferRequest;
import com.myZipPlan.server.user.userService.UserService;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse transferTempUserToLoginUser(@RequestBody TempUserTransferRequest request) {
        userService.transferTempUserToLoginUser(request.getTempUserId());

        return ApiResponse.success();


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
}
