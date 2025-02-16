package com.myZipPlan.server;

import com.myZipPlan.server.advice.aiReportGenerator.service.AiReportGeneratorService;
import com.myZipPlan.server.advice.aiReportGenerator.service.OpenAiApiService;
import com.myZipPlan.server.advice.loanAdvice.controller.LoanAdviceApiController;
import com.myZipPlan.server.advice.loanAdvice.service.LoanAdviceQueryService;
import com.myZipPlan.server.advice.loanAdvice.service.LoanAdviceService;
import com.myZipPlan.server.calculator.dtiCalc.service.DtiCalcService;
import com.myZipPlan.server.calculator.repaymentCalc.controller.RepaymentCalcApiController;
import com.myZipPlan.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.myZipPlan.server.oauth.config.SecurityPathConfig;
import com.myZipPlan.server.oauth.jwt.JwtAccessDeniedHandler;
import com.myZipPlan.server.oauth.jwt.JwtAuthenticationFailEntryPoint;
import com.myZipPlan.server.oauth.jwt.JwtAuthenticationFilter;
import com.myZipPlan.server.oauth.jwt.Oauth2SuccessHandler;
import com.myZipPlan.server.oauth.token.TokenProvider;
import com.myZipPlan.server.oauth.token.TokenValidator;
import com.myZipPlan.server.oauth.userInfo.KakaoUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myZipPlan.server.user.userService.GuestService;
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
    RepaymentCalcApiController.class,  LoanAdviceApiController.class})
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
    protected LoanAdviceQueryService loanAdviceQueryService;
    @MockBean
    protected AiReportGeneratorService aiReportGeneratorService;
    @MockBean
    protected OpenAiApiService openAiApiService;
    @MockBean
    protected GuestService guestService;

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
