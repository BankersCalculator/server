package com.bankersCalculator.server;

import com.bankersCalculator.server.repaymentCalc.controller.RepaymentCalcApiController;
import com.bankersCalculator.server.repaymentCalc.service.RepaymentCalcService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RepaymentCalcApiController.class)
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    // 서비스를 이곳에서 관리할지말지는 추후 고민해보자.
    @MockBean
    protected RepaymentCalcService repaymentCalcService;
}
