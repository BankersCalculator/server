package com.bankersCalculator.server;

import com.bankersCalculator.server.calculator.dtiCalc.controller.DtiCalcController;
import com.bankersCalculator.server.calculator.dtiCalc.service.DtiCalcService;
import com.bankersCalculator.server.calculator.repaymentCalc.controller.RepaymentCalcApiController;
import com.bankersCalculator.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.bankersCalculator.server.oauth.config.SecurityConfig;
import com.bankersCalculator.server.oauth.jwt.JwtAccessDeniedHandler;
import com.bankersCalculator.server.oauth.jwt.JwtAuthenticationFailEntryPoint;
import com.bankersCalculator.server.oauth.jwt.JwtAuthenticationFilter;
import com.bankersCalculator.server.oauth.jwt.Oauth2SuccessHandler;
import com.bankersCalculator.server.oauth.token.TokenProvider;
import com.bankersCalculator.server.oauth.token.TokenValidator;
import com.bankersCalculator.server.oauth.userInfo.KakaoUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@WithMockUser(roles = "USER")
@WebMvcTest(controllers = {
    RepaymentCalcApiController.class, DtiCalcController.class})
@Import(SecurityConfig.class)
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    // 서비스를 이곳에서 관리할지말지는 추후 고민해보자.
    @MockBean
    protected RepaymentCalcService repaymentCalcService;
    @MockBean
    protected DtiCalcService dtiCalcService;
    @MockBean
    protected JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    protected TokenValidator tokenValidator;
    @MockBean
    protected TokenProvider tokenProvider;
    @MockBean
    protected KakaoUserDetailsService kakaoUserDetailsService;
    @MockBean
    protected Oauth2SuccessHandler oauth2SuccessHandler;
    @MockBean
    protected JwtAuthenticationFailEntryPoint jwtAuthenticationFailEntryPoint;
    @MockBean
    protected JwtAccessDeniedHandler jwtAccessDeniedHandler;
}
