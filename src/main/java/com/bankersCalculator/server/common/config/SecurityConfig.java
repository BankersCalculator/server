package com.bankersCalculator.server.common.config;


import com.bankersCalculator.server.common.oauth.jwt.JwtAuthenticationFilter;
import com.bankersCalculator.server.common.oauth.jwt.JwtAccessDeniedHandler;
import com.bankersCalculator.server.common.oauth.jwt.JwtAuthenticationFailEntryPoint;
import com.bankersCalculator.server.common.oauth.jwt.Oauth2SuccessHandler;
import com.bankersCalculator.server.common.oauth.token.TokenProvider;
import com.bankersCalculator.server.common.oauth.token.TokenValidator;
import com.bankersCalculator.server.common.oauth.user.KakaoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Slf4j
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final KakaoUserDetailsService kakaoUserDetailsService;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final JwtAuthenticationFailEntryPoint jwtAuthenticationFailEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final SecurityPathConfig securityPathConfig;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers(PathRequest.toH2Console())
            .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
            .requestMatchers("/favicon.ico", "/error", "/api/v1/auth/**", "/docs/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .headers(headers -> headers
                .frameOptions(FrameOptionsConfig::sameOrigin)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(securityPathConfig.getPublicPaths().toArray(new String[0])).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oAuth2Login -> {
                    oAuth2Login.userInfoEndpoint(userInfoEndpointConfig ->
                            userInfoEndpointConfig.userService(kakaoUserDetailsService))
                            .successHandler(oauth2SuccessHandler);
            })
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> {
                ex.authenticationEntryPoint(jwtAuthenticationFailEntryPoint);
                ex.accessDeniedHandler(jwtAccessDeniedHandler);});


        return http.build();
    }

}
