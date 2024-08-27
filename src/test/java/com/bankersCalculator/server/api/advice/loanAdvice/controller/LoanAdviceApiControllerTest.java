package com.bankersCalculator.server.api.advice.loanAdvice.controller;

import com.bankersCalculator.server.ControllerTestSupport;
import com.bankersCalculator.server.advice.loanAdvice.dto.request.LoanAdviceRequest;
import com.bankersCalculator.server.common.enums.loanAdvice.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvice.MaritalStatus;
import com.bankersCalculator.server.common.enums.calculator.HouseOwnershipType;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoanAdviceApiControllerTest extends ControllerTestSupport {

    private static final String BASE_URL = "/api/v1/loanAdvice";

    @DisplayName("대출 상담 정상 테스트")
    @Test
    void generateLoanAdvice() throws Exception {
        // given
        LoanAdviceRequest request = LoanAdviceRequest.builder()
            .rentalDeposit(new BigDecimal("200000000"))
            .monthlyRent(new BigDecimal("500000"))
            .cashOnHand(new BigDecimal("50000000"))
            .age(35)
            .maritalStatus(MaritalStatus.MARRIED)
            .annualIncome(new BigDecimal("60000000"))
            .spouseAnnualIncome(new BigDecimal("40000000"))
            .childStatus(ChildStatus.ONE_CHILD)
            .hasNewborn(false)
            .houseOwnershipType(HouseOwnershipType.NO_HOUSE)
            .isSMEEmployee(true)
            .isNetAssetOver345M(false)
            .rentHousingType(RentHousingType.APARTMENT)
            .exclusiveArea(84.5)
            .buildingName("행복아파트")
            .districtCode("1111011700")
            .dongName("역삼동")
            .jibun("649-5")
            .build();

        // when & then
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
}