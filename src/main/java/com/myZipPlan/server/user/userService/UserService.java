package com.myZipPlan.server.user.userService;

import com.myZipPlan.server.common.exception.customException.AuthException;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;


    public User save(User user) {
        return userRepository.save(user);
    }

    public User findUser(String providerId) {

        return userRepository.findByOauthProviderId(providerId)
            .orElseThrow(() -> new AuthException("사용자 정보가 없습니다."));
    }


    public void transferTempUserToLoginUser(String tempUserId) {
    }

    public void logout() {
    }

    public void withdraw() {
    }
}
