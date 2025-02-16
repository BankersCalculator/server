package com.myZipPlan.server.user.userService;

import com.myZipPlan.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.myZipPlan.server.advice.loanAdvice.service.LoanAdviceService;
import com.myZipPlan.server.advice.userInputInfo.repository.UserInputInfoRepository;
import com.myZipPlan.server.advice.userInputInfo.service.UserInputInfoService;
import com.myZipPlan.server.common.exception.customException.AuthException;
import com.myZipPlan.server.oauth.userInfo.SecurityUtils;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserInputInfoRepository userInputInfoRepository;
    private final LoanAdviceResultRepository loanAdviceResultRepository;


    public User save(User user) {
        return userRepository.save(user);
    }

    public User findUser(String providerId) {

        return userRepository.findByProviderId(providerId)
            .orElseThrow(() -> new AuthException("사용자 정보가 없습니다."));
    }


    @Transactional
    public void transferTempUserToLoginUser(String tempUserId) {

        User tempUser = findUser(tempUserId);
        User user = fetchCurrentUser();

        userInputInfoRepository.updateUserFromTempUser(tempUser, user);
        loanAdviceResultRepository.updateUserFromTempUser(tempUser, user);
        userRepository.delete(tempUser);
    }

    public void logout() {
    }

    public void withdraw() {
    }


    private User fetchCurrentUser() {
        String providerId = SecurityUtils.getProviderId();
        return findUser(providerId);
    }
}
