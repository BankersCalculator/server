package com.bankersCalculator.server.repaymentCalc.controller;

import com.bankersCalculator.server.ControllerTestSupport;
import com.bankersCalculator.server.common.enums.RepaymentType;
import com.bankersCalculator.server.repaymentCalc.dto.RepaymentCalcRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class RepaymentCalcApiControllerTest extends ControllerTestSupport {

    private static final String BASE_URL = "/api/v1/repaymentCalc";

    @DisplayName("균등분할상환 정상 테스트")
    @Test
    void CalcRepaymentOnAmortizingLoanTest() throws Exception {
        //given
        RepaymentCalcRequest request = RepaymentCalcRequest.builder()
            .repaymentType(RepaymentType.AMORTIZING)
            .principal(300000000)
            .term(60)
            .gracePeriod(0)
            .interestRatePercentage(4)
            .maturityPaymentAmount(0)
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsBytes(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(status().isOk());
    }

}