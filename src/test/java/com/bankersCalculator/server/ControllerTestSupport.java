package com.bankersCalculator.server;

import com.bankersCalculator.server.advice.loanAdvice.controller.LoanAdviceApiController;
import com.bankersCalculator.server.advice.loanAdvice.service.LoanAdviceService;
import com.bankersCalculator.server.calculator.dtiCalc.controller.DtiCalcController;
import com.bankersCalculator.server.calculator.dtiCalc.service.DtiCalcService;
import com.bankersCalculator.server.calculator.repaymentCalc.controller.RepaymentCalcApiController;
import com.bankersCalculator.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.bankersCalculator.server.oauth.config.SecurityPathConfig;
import com.bankersCalculator.server.oauth.jwt.JwtAccessDeniedHandler;
import com.bankersCalculator.server.oauth.jwt.JwtAuthenticationFailEntryPoint;
import com.bankersCalculator.server.oauth.jwt.JwtAuthenticationFilter;
import com.bankersCalculator.server.oauth.jwt.Oauth2SuccessHandler;
import com.bankersCalculator.server.oauth.token.TokenProvider;
import com.bankersCalculator.server.oauth.token.TokenValidator;
import com.bankersCalculator.server.oauth.userInfo.KakaoUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@WithMockUser(roles = "USER")
@WebMvcTest(controllers = {
    RepaymentCalcApiController.class, DtiCalcController.class, LoanAdviceApiController.class})
public abstract class ControllerTestSupport {


    @Autowired
    private WebApplicationContext context;
    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }
    // 서비스를 이곳에서 관리할지말지는 추후 고민해보자.
    @MockBean
    protected RepaymentCalcService repaymentCalcService;
    @MockBean
    protected DtiCalcService dtiCalcService;
    @MockBean
    protected LoanAdviceService loanAdviceService;


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
    @MockBean
    protected SecurityPathConfig securityPathConfig;
}
