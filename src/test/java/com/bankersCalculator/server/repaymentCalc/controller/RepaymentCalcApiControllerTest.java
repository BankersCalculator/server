package com.bankersCalculator.server.repaymentCalc.controller;

import com.bankersCalculator.server.ControllerTestSupport;
import com.bankersCalculator.server.common.enums.RepaymentType;
import com.bankersCalculator.server.calculator.repaymentCalc.dto.RepaymentCalcRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.bankersCalculator.server.common.message.ValidationMessage.MAX_VALUE_INTEREST_RATE_PERCENTAGE;
import static com.bankersCalculator.server.common.message.ValidationMessage.NOT_NULL_REPAYMENT_TYPE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class RepaymentCalcApiControllerTest extends ControllerTestSupport {

    private static final String BASE_URL = "/api/v1/repaymentCalc";

    @DisplayName("균등분할상환 정상 테스트")
    @Test
    void CalcRepaymentOnAmortizingLoan() throws Exception {
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

    @DisplayName("만기일시상환 정상 테스트")
    @Test
    void CalcRepaymentOnBulletLoan() throws Exception {
        //given
        RepaymentCalcRequest request = RepaymentCalcRequest.builder()
            .repaymentType(RepaymentType.BULLET)
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

    @DisplayName("균등분할상환 비정상 테스트")
    @Test
    void CalcRepaymentOnAmortizingLoanWithoutRepaymentType() throws Exception {
        //given
        RepaymentCalcRequest request = RepaymentCalcRequest.builder()
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
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value(NOT_NULL_REPAYMENT_TYPE));
    }

    @DisplayName("만기일시상환 비정상 테스트")
    @Test
    void CalcRepaymentOnBulletLoanWithBadInterestRate() throws Exception {
        //given
        RepaymentCalcRequest request = RepaymentCalcRequest.builder()
            .repaymentType(RepaymentType.BULLET)
            .principal(300000000)
            .term(60)
            .gracePeriod(0)
            .interestRatePercentage(77)
            .maturityPaymentAmount(0)
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsBytes(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value(MAX_VALUE_INTEREST_RATE_PERCENTAGE));
    }



}