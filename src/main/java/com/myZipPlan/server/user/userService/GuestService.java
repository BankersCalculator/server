package com.myZipPlan.server.user.userService;

import com.myZipPlan.server.common.enums.RoleType;
import com.myZipPlan.server.oauth.token.TokenDto;
import com.myZipPlan.server.oauth.token.TokenProvider;
import com.myZipPlan.server.oauth.userInfo.KakaoUserDetails;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.GuestUsageRedisRepository;
import com.myZipPlan.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final UserRepository userRepository;
    private final GuestUsageRedisRepository guestUsageRedisRepository;
    private final TokenProvider tokenProvider;

    public TokenDto registerGuest() {
        User guest = User.createGuestUser();
        userRepository.save(guest);

        TokenDto token = tokenProvider.createToken(guest.getProviderId(), guest.getRoleType().getCode());
        return token;
    }

    public void increaseCount(String guestId) {
        guestUsageRedisRepository.incrementCount(guestId);
    }
}
