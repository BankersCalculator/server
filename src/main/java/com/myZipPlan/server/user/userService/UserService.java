package com.myZipPlan.server.user.userService;

import com.myZipPlan.server.advice.userInputInfo.repository.UserInputInfoRepository;
import com.myZipPlan.server.advice.userInputInfo.service.UserInputInfoService;
import com.myZipPlan.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserInputInfoService userInputInfoService;





    public void transferTempUserToLoginUser(String tempUserId) {
    }

    public void logout() {
    }

    public void withdraw() {
    }
}
