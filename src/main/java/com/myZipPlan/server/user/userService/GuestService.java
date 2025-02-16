package com.myZipPlan.server.user.userService;

import com.myZipPlan.server.oauth.token.TokenDto;
import com.myZipPlan.server.oauth.token.TokenProvider;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public TokenDto registerGuest() {
        User guest = User.createGuestUser();
        userRepository.save(guest);
        return tokenProvider.createToken(guest.getProviderId(), guest.getRoleType().name());
    }
}
