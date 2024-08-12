package com.bankersCalculator.server.common.config;


import com.bankersCalculator.server.common.oauth.KakaoUserDetailsService;
import com.bankersCalculator.server.common.oauth.Oauth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@RequiredArgsConstructor
@Slf4j
@EnableWebSecurity
public class SecurityConfig {

    private final KakaoUserDetailsService kakaoUserDetailsService;
    private final Oauth2SuccessHandler oauth2SuccessHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
            .requestMatchers("/favicon.ico", "/error", "/api/v1/login", "/docs/**");
    }

    @Bean
    public HttpSecurity filterChain(HttpSecurity http) throws Exception {
        return http.oauth2Login(oAuth2Login -> {
            oAuth2Login.userInfoEndpoint(userInfoEndpointConfig ->
                userInfoEndpointConfig.userService(kakaoUserDetailsService))
                .successHandler(oauth2SuccessHandler);
        });
    }


}
