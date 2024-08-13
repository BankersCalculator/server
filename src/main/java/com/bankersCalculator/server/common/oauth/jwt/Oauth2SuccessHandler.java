package com.bankersCalculator.server.common.oauth.jwt;

import com.bankersCalculator.server.common.oauth.token.TokenDto;
import com.bankersCalculator.server.common.oauth.token.TokenProvider;
import com.bankersCalculator.server.common.oauth.user.KakaoUserInfo;
import com.bankersCalculator.server.user.domain.User;
import com.bankersCalculator.server.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // TODO: 수정 요망
    private static final String REDIRECT_URI = "http://localhost:8080/api/user/login/kakao?accessToken=%s&refreshToken=%s";

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        String id = kakaoUserInfo.getId();
        String provider = kakaoUserInfo.getProvider();

        // TODO: exception 커스텀할 것.
        User user = userRepository.findByOauthProviderAndOauthProviderId(provider, id)
            .orElseThrow(ServletException::new);

        TokenDto tokenDto = tokenProvider.createToken(provider, id, user.getRoleType().getCode());

        String redirectURI = String.format(REDIRECT_URI,
            tokenDto.getAccessToken(), tokenDto.getRefreshToken());

        getRedirectStrategy().sendRedirect(request, response, redirectURI);
    }
}
