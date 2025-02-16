package com.myZipPlan.server.user.userService;

import com.myZipPlan.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.myZipPlan.server.advice.userInputInfo.repository.UserInputInfoRepository;
import com.myZipPlan.server.common.exception.customException.AuthException;
import com.myZipPlan.server.oauth.token.TokenProvider;
import com.myZipPlan.server.oauth.userInfo.SecurityUtils;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserInputInfoRepository userInputInfoRepository;
    private final LoanAdviceResultRepository loanAdviceResultRepository;
    private final TokenProvider tokenProvider;


    public User save(User user) {
        return userRepository.save(user);
    }

    public User findUser(String providerId) {

        return userRepository.findByProviderId(providerId)
            .orElseThrow(() -> new AuthException("사용자 정보가 없습니다."));
    }

    public User getCurrentUser() {
        String providerId = SecurityUtils.getProviderId();
        return findUser(providerId);
    }


    @Transactional
    public void transferGuestToUser(String guestToken) {

        Authentication authentication = tokenProvider.getAuthentication(guestToken);
        String guestProviderId = SecurityUtils.getGuestProviderId(authentication);

        User guest = findUser(guestProviderId);
        User user = fetchCurrentUser();

        userInputInfoRepository.updateUserFromGuest(guest, user);
        loanAdviceResultRepository.updateUserFromGuest(guest, user);
        userRepository.delete(guest);
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
