package com.bankersCalculator.server.api.advice.loanAdvice.service;

import com.bankersCalculator.server.IntegrationTestSupport;
import com.bankersCalculator.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.bankersCalculator.server.advice.loanAdvice.service.LoanAdviceService;
import com.bankersCalculator.server.common.enums.Bank;
import com.bankersCalculator.server.common.enums.RoleType;
import com.bankersCalculator.server.common.enums.loanAdvice.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvice.MaritalStatus;
import com.bankersCalculator.server.common.enums.calculator.HouseOwnershipType;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import com.bankersCalculator.server.oauth.userInfo.KakaoUserDetails;
import com.bankersCalculator.server.user.entity.User;
import com.bankersCalculator.server.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoanAdviceServiceTest extends IntegrationTestSupport {

    /**
     * TODO: LoanAdvice 상품을 추가할 때마다 테스트 결과가 달라질 수 있음.
     * TODO: MVP 단계에서 취급할 상품 전부 구현한 후 테스트코드 고도화할 것.
     */

    @Autowired
    LoanAdviceService loanAdviceService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Security 검증을 위한 테스트 유저 생성
        createTestUser();
    }


    @DisplayName("대출 상담 정상 산출 테스트")
    @Test
    void generateLoanAdvice() {
        // given
        LoanAdviceServiceRequest request = LoanAdviceServiceRequest.builder()
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
            .exclusiveArea(new BigDecimal(84.5))
            .buildingName("행복아파트")
            .districtCode("1111011700")
            .dongName("역삼동")
            .jibun("649-5")
            .build();

        // when
        LoanAdviceResponse response = loanAdviceService.generateLoanAdvice(request);

        // then
        assertNotNull(response);
//        assertEquals("서울시신혼부부임차보증금대출", response.getLoanProductName());
//        assertEquals("HF-01", response.getLoanProductCode());
//        assertEquals(new BigDecimal("180000000.0"), response.getPossibleLoanLimit());
//        assertEquals(new BigDecimal("4.60"), response.getExpectedLoanRate());
//        assertEquals(new BigDecimal("200000000.0"), response.getTotalRentalDeposit());
//        assertEquals(new BigDecimal("180000000.0"), response.getLoanAmount());
//        assertEquals(new BigDecimal("20000000.0"), response.getOwnFunds());
//        assertEquals(new BigDecimal("690000"), response.getMonthlyInterestCost());
//        assertEquals(new BigDecimal("500000"), response.getMonthlyRent());
//        assertEquals(new BigDecimal("0.0"), response.getOpportunityCostOwnFunds());
//        assertEquals(new BigDecimal("0.03"), response.getDepositInterestRate());
//        assertEquals(new BigDecimal("180000.0000"), response.getGuaranteeInsuranceFee());
//        assertEquals(new BigDecimal("75000"), response.getStampDuty());
//        assertEquals("AI가 생성해 줄 보고서입니다.", response.getRecommendationReason());
//
//        assertNotNull(response.getRecommendedProducts());
//        assertTrue(response.getRecommendedProducts().size() >= 1);
//        assertEquals("고정금리 협약전세자금보증", response.getRecommendedProducts().get(0).getLoanProductName());
//        assertEquals("HF-07", response.getRecommendedProducts().get(0).getLoanProductCode());
//        assertEquals(new BigDecimal("300000"), response.getRecommendedProducts().get(0).getPossibleLoanLimit());
//        assertEquals(new BigDecimal("4.60"), response.getRecommendedProducts().get(0).getExpectedLoanRate());
//        assertEquals("부부합산소득 1.3억 초과시 대출 불가능합니다.", response.getRecommendedProducts().get(0).getNotEligibleReasons().get(0));
//
//        assertNotNull(response.getAvailableBanks());
//        assertTrue(response.getAvailableBanks().contains(Bank.HANA));
//        assertTrue(response.getAvailableBanks().contains(Bank.SHINHAN));
//        assertTrue(response.getAvailableBanks().contains(Bank.KB));
//
//        assertEquals("전세대출 가이드", response.getRentalLoanGuide());
    }

    private void createTestUser() {
        KakaoUserDetails userDetails = mock(KakaoUserDetails.class);
        when(userDetails.getName()).thenReturn("test_provider_id");

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = User.create("Kakao", "test_provider_id", "test_email", RoleType.USER);
        userRepository.save(user);
    }
}