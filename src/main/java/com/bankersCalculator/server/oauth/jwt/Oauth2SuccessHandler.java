package com.bankersCalculator.server.oauth.jwt;

import com.bankersCalculator.server.oauth.repository.RefreshTokenRedisRepository;
import com.bankersCalculator.server.oauth.token.RefreshToken;
import com.bankersCalculator.server.oauth.token.TokenDto;
import com.bankersCalculator.server.oauth.token.TokenProvider;
import com.bankersCalculator.server.oauth.userInfo.KakaoUserInfo;
import com.bankersCalculator.server.user.domain.User;
import com.bankersCalculator.server.user.repository.UserRepository;
import jakarta.servlet.ServletException;
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

    // 인증 완료 후 Client에게 토큰반환할 Controller 주소
    private static final String REDIRECT_URI = "/login/oauth2/kakao?accessToken=%s&refreshToken=%s";

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

        // TODO: exception 커스텀할 것.
        User user = userRepository.findByOauthProviderAndEmail(provider, email)
            .orElseThrow(ServletException::new);


        TokenDto tokenDto = tokenProvider.createToken(email, user.getRoleType().getCode());

        saveRefreshTokenOnRedis(user, tokenDto);

        String redirectURI = String.format(REDIRECT_URI,
            tokenDto.getAccessToken(), tokenDto.getRefreshToken());

        getRedirectStrategy().sendRedirect(request, response, redirectURI);
    }

    private void saveRefreshTokenOnRedis(User user, TokenDto tokenDto) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoleType().getCode()));

        refreshTokenRedisRepository.save(RefreshToken.builder()
            .email(user.getEmail())
            .authorities(authorities)
            .refreshToken(tokenDto.getRefreshToken())
            .build());
    }
}
