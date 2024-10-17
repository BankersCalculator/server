package com.myZipPlan.server.api.advice.loanAdvice.controller;

import com.myZipPlan.server.ControllerTestSupport;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceRequest;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.myZipPlan.server.advice.loanAdvice.dto.response.RecommendedProductDto;
import com.myZipPlan.server.common.enums.Bank;
import com.myZipPlan.server.common.enums.loanAdvice.ChildStatus;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseHouseOwnershipType;
import com.myZipPlan.server.common.enums.loanAdvice.MaritalStatus;
import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.housingInfo.buildingInfo.common.RentHousingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoanAdviceApiControllerTest extends ControllerTestSupport {

    private static final String BASE_URL = "/api/v1/loanAdvice";

    @DisplayName("대출 상담 정상 테스트")
    @Test
    void createLoanAdvice() throws Exception {
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
            .houseOwnershipType(JeonseHouseOwnershipType.NO_HOUSE)
            .isSMEEmployee(true)
            .isNetAssetOver345M(false)
            .rentHousingType(RentHousingType.APARTMENT)
            .exclusiveArea(BigDecimal.valueOf(84.5))
            .buildingName("행복아파트")
            .districtCode("1111011700")
            .dongName("역삼동")
            .jibun("649-5")
            .build();

        LoanAdviceResponse sampleLoanAdviceResponse = createSampleLoanAdviceResponse();

        when(loanAdviceService.createLoanAdvice(any())).thenReturn(sampleLoanAdviceResponse);

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

    private LoanAdviceResponse createSampleLoanAdviceResponse() {
        return LoanAdviceResponse.builder()
            .loanAdviceResultId(1L)
            .hasEligibleProduct(true)
            .loanProductName("샘플 전세자금대출")
            .loanProductCode("SAMPLE001")
            .possibleLoanLimit(BigDecimal.valueOf(200000000))
            .expectedLoanRate(BigDecimal.valueOf(3.5))
            .totalRentalDeposit(BigDecimal.valueOf(300000000))
            .loanAmount(BigDecimal.valueOf(200000000))
            .ownFunds(BigDecimal.valueOf(100000000))
            .monthlyInterestCost(BigDecimal.valueOf(583333))
            .monthlyRent(BigDecimal.valueOf(0L))
            .totalLivingCost(BigDecimal.valueOf(583333L))
            .opportunityCostOwnFunds(BigDecimal.valueOf(100000000))
            .depositInterestRate(BigDecimal.valueOf(2.5))
            .guaranteeInsuranceFee(BigDecimal.valueOf(1000000))
            .stampDuty(BigDecimal.valueOf(150000))
            .recommendationReason("고객님의 소득과 신용도를 고려하여 가장 적합한 상품으로 선정되었습니다.")
            .recommendedProducts(Arrays.asList(
                RecommendedProductDto.builder()
                    .loanProductName("신혼부부전용전세자금대출")
                    .loanProductCode("HF-001")
                    .possibleLoanLimit(BigDecimal.valueOf(180000000))
                    .expectedLoanRate(BigDecimal.valueOf(3.7))
                    .notEligibleReasons(List.of())
                    .build(),
                RecommendedProductDto.builder()
                    .loanProductName("서울시신혼부부임차보증금대출")
                    .loanProductCode("HF-002")
                    .possibleLoanLimit(BigDecimal.valueOf(220000000))
                    .expectedLoanRate(BigDecimal.valueOf(3.8))
                    .notEligibleReasons(List.of("임차목적지가 서울시가 아닙니다."))
                    .build()
            ))
            .availableBanks(Arrays.asList(Bank.KB, Bank.SHINHAN, Bank.WOORI))
            .rentalLoanGuide("전세자금대출 이용 시 주의사항:\n1. 대출 기간 동안 이자를 꾸준히 납부해야 합니다.\n2. 전세 계약 만료 시 대출금 상환 계획을 미리 세워야 합니다.")
            .build();
    }
}