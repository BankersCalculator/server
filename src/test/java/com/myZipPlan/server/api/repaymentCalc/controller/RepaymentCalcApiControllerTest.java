package com.myZipPlan.server.api.repaymentCalc.controller;

import com.myZipPlan.server.ControllerTestSupport;
import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcRequest;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static com.myZipPlan.server.common.message.ValidationMessage.MAX_VALUE_INTEREST_RATE_PERCENTAGE;
import static com.myZipPlan.server.common.message.ValidationMessage.NOT_NULL_REPAYMENT_TYPE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
            .principal(BigDecimal.valueOf(300000000))
            .term(BigDecimal.valueOf(60))
            .gracePeriod(BigDecimal.valueOf(0))
            .interestRatePercentage(BigDecimal.valueOf(4))
            .maturityPaymentAmount(BigDecimal.valueOf(0))
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsBytes(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
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
            .principal(BigDecimal.valueOf(300000000))
            .term(BigDecimal.valueOf(60))
            .gracePeriod(BigDecimal.valueOf(0))
            .interestRatePercentage(BigDecimal.valueOf(4))
            .maturityPaymentAmount(BigDecimal.valueOf(0))
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsBytes(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
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
            .principal(BigDecimal.valueOf(300000000))
            .term(BigDecimal.valueOf(60))
            .gracePeriod(BigDecimal.valueOf(0))
            .interestRatePercentage(BigDecimal.valueOf(4))
            .maturityPaymentAmount(BigDecimal.valueOf(0))
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsBytes(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
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
            .principal(BigDecimal.valueOf(300000000))
            .term(BigDecimal.valueOf(60))
            .gracePeriod(BigDecimal.valueOf(0))
            .interestRatePercentage(BigDecimal.valueOf(77))
            .maturityPaymentAmount(BigDecimal.valueOf(0))
            .build();

        //when //then
        mockMvc.perform(
                post(BASE_URL)
                    .content(objectMapper.writeValueAsBytes(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value(MAX_VALUE_INTEREST_RATE_PERCENTAGE));
    }
}