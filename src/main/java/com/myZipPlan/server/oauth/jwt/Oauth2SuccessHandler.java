package com.myZipPlan.server.oauth.jwt;

import com.myZipPlan.server.oauth.repository.RefreshTokenRedisRepository;
import com.myZipPlan.server.oauth.token.RefreshToken;
import com.myZipPlan.server.oauth.token.TokenDto;
import com.myZipPlan.server.oauth.token.TokenProvider;
import com.myZipPlan.server.oauth.userInfo.KakaoUserInfo;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7일
    // 인증 완료 후 Client에게 토큰반환할 URI
    private static final String REDIRECT_URI = "http://localhost:5173/login-result";

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());

        String provider = kakaoUserInfo.getProvider();
        String email = kakaoUserInfo.getEmail();
        String providerId = kakaoUserInfo.getProviderId();

        User user = userRepository.findByOauthProviderAndOauthProviderId(provider, providerId)
            .orElseThrow(ServletException::new);



        TokenDto tokenDto = tokenProvider.createToken(providerId, user.getRoleType().getCode());

        saveRefreshTokenOnRedis(user, tokenDto);

        addTokenCookie(response, "accessToken", tokenDto.getAccessToken());
        addTokenCookie(response, "refreshToken", tokenDto.getRefreshToken());

        getRedirectStrategy().sendRedirect(request, response, REDIRECT_URI);
    }

    private void addTokenCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void saveRefreshTokenOnRedis(User user, TokenDto tokenDto) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoleType().getCode()));

        refreshTokenRedisRepository.save(RefreshToken.builder()
            .providerId(user.getOauthProviderId())
            .authorities(authorities)
            .refreshToken(tokenDto.getRefreshToken())
            .build());
    }
}
