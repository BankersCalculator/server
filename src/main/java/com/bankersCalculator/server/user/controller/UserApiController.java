package com.bankersCalculator.server.user.controller;

import com.bankersCalculator.server.user.UserService.UserService;
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
    public void transferTempUserToLoginUser(@RequestBody String tempUserId) {
        userService.transferTempUserToLoginUser(tempUserId);
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
