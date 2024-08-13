package com.bankersCalculator.server.oauth.config;


import com.bankersCalculator.server.oauth.jwt.JwtAccessDeniedHandler;
import com.bankersCalculator.server.oauth.jwt.JwtAuthenticationFailEntryPoint;
import com.bankersCalculator.server.oauth.jwt.JwtAuthenticationFilter;
import com.bankersCalculator.server.oauth.jwt.Oauth2SuccessHandler;
import com.bankersCalculator.server.oauth.token.TokenProvider;
import com.bankersCalculator.server.oauth.token.TokenValidator;
import com.bankersCalculator.server.oauth.userInfo.KakaoUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Slf4j
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final KakaoUserDetailsService kakaoUserDetailsService;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final JwtAuthenticationFailEntryPoint jwtAuthenticationFailEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers(PathRequest.toH2Console())
            .requestMatchers("/","/error", "/favicon.ico", "/exception/**", "login/oauth2/*");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.debug("helllooo~~~?????");


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
                    .requestMatchers("/login/oauth2/code/*").permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(kakaoUserDetailsService)
                )
                .successHandler(oauth2SuccessHandler)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> {
                ex.authenticationEntryPoint(authenticationEntryPoint());
                ex.accessDeniedHandler(jwtAccessDeniedHandler);
            });
        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            log.error("Unauthorized error: {}", authException.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "아 모르겠다");
        };
    }

}
