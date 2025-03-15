package com.myZipPlan.server.user.userService;

import com.myZipPlan.server.common.enums.ABTestType;
import com.myZipPlan.server.common.enums.RoleType;
import com.myZipPlan.server.oauth.token.TokenDto;
import com.myZipPlan.server.oauth.token.TokenProvider;
import com.myZipPlan.server.oauth.userInfo.KakaoUserDetails;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.GuestUsage;
import com.myZipPlan.server.user.repository.GuestUsageRedisRepository;
import com.myZipPlan.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
    private final ABTestService abTestService;

    public TokenDto registerGuest() {
        ABTestType abTestType = abTestService.assignABGroup();
        User guest = User.createGuestUser(abTestType);
        userRepository.save(guest);

        TokenDto token = tokenProvider.createToken(guest.getProviderId(), guest.getRoleType().getCode());
        token.setAbTestType(abTestType);
        return token;
    }

    public void canUseGuestFeature(String guestId) {
        GuestUsage guestUsage = guestUsageRedisRepository.incrementCount(guestId);
        if (guestUsage.getCount() > 10) {
            throw new AccessDeniedException("게스트 유저는 10회까지만 이용 가능합니다");
        }
    }
}
