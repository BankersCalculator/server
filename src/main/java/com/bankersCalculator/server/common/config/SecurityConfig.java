package com.bankersCalculator.server.common.config;


import com.bankersCalculator.server.common.oauth.filter.JwtFilter;
import com.bankersCalculator.server.common.oauth.jwt.JwtAccessDeniedHandler;
import com.bankersCalculator.server.common.oauth.jwt.JwtAuthenticationFailEntryPoint;
import com.bankersCalculator.server.common.oauth.jwt.Oauth2SuccessHandler;
import com.bankersCalculator.server.common.oauth.user.KakaoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Slf4j
//@EnableWebSecurity
public class SecurityConfig {

    private final KakaoUserDetailsService kakaoUserDetailsService;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final JwtFilter jwtFilter;
    private final JwtAuthenticationFailEntryPoint jwtAuthenticationFailEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final SecurityPathConfig securityPathConfig;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers("/h2-console/**", "/favicon.ico")
            .requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }

    @Bean
    public HttpSecurity filterChain(HttpSecurity http) throws Exception {

        http.cors(AbstractHttpConfigurer::disable)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .ignoringRequestMatchers("/h2-console/**")
                .disable())
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .headers(headers -> headers
                .frameOptions(FrameOptionsConfig::sameOrigin)
            )
            .oauth2Login(oAuth2Login -> {
                oAuth2Login.userInfoEndpoint(userInfoEndpointConfig ->
                        userInfoEndpointConfig.userService(kakaoUserDetailsService))
                    .successHandler(oauth2SuccessHandler);
            })
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                .requestMatchers(securityPathConfig.getPublicPaths().toArray(new String[0])).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> {
                ex.authenticationEntryPoint(jwtAuthenticationFailEntryPoint);
                ex.accessDeniedHandler(jwtAccessDeniedHandler);
            });

        return http;
    }

}
