package com.bankersCalculator.server.oauth.jwt;

import com.bankersCalculator.server.oauth.token.TokenDto;
import com.bankersCalculator.server.oauth.token.TokenProvider;
import com.bankersCalculator.server.oauth.userInfo.KakaoUserInfo;
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
@Slf4j
@Component
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // TODO: 수정 요망
    private static final String REDIRECT_URI = "http://localhost:8080/login/oauth2/kakao?accessToken=%s&refreshToken=%s";
    private static final String URI = "/auth/success";

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        String id = kakaoUserInfo.getEmail();
        String provider = kakaoUserInfo.getProvider();
        String email = kakaoUserInfo.getEmail();

        // TODO: exception 커스텀할 것.
        User user = userRepository.findByOauthProviderAndEmail(provider, email)
            .orElseThrow(ServletException::new);


        TokenDto tokenDto = tokenProvider.createToken(provider, id, user.getRoleType().getCode());

        String redirectURI = String.format(REDIRECT_URI,
            tokenDto.getAccessToken(), tokenDto.getRefreshToken());

//        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
//            .queryParam("accessToken", tokenDto.getAccessToken())
//            .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectURI);
    }
}
