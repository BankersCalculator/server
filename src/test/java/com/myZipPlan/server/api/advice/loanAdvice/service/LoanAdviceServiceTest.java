package com.myZipPlan.server.api.advice.loanAdvice.service;

import com.myZipPlan.server.IntegrationTestSupport;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.myZipPlan.server.advice.loanAdvice.service.LoanAdviceService;
import com.myZipPlan.server.common.enums.RoleType;
import com.myZipPlan.server.common.enums.loanAdvice.ChildStatus;
import com.myZipPlan.server.common.enums.loanAdvice.MaritalStatus;
import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import com.myZipPlan.server.oauth.userInfo.KakaoUserDetails;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
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


    // TODO: 조회, 실패 등 테스트케이스 추가할 것.
    // 상품이 추가될 때마다 테스트 결과가 바뀔 수 있다는 게 찜찜하다.
    // 어떻게 해결할 수 있을지 고민해볼 것..

    @DisplayName("일반 대출추천 - 서울시신혼부부(HF-01)")
    @Test
    void generateLoanAdvice_targetHF01() {
        // given
        LoanAdviceServiceRequest request = LoanAdviceServiceRequest.builder()
            .rentalDeposit(new BigDecimal("300000000"))
            .monthlyRent(new BigDecimal("0"))
            .cashOnHand(new BigDecimal("50000000"))
            .age(35)
            .maritalStatus(MaritalStatus.ENGAGED)
            .annualIncome(new BigDecimal("45000000"))
            .spouseAnnualIncome(new BigDecimal("40000000"))
            .childStatus(ChildStatus.NO_CHILD)
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
        LoanAdviceResponse response = loanAdviceService.createLoanAdvice(request);

        // then
        assertNotNull(response);
        assertEquals("HF-01", response.getLoanProductCode());
        assertEquals("서울시신혼부부임차보증금대출", response.getLoanProductName());

        assertEquals(new BigDecimal("270000000"), response.getPossibleLoanLimit());
        assertEquals(new BigDecimal("4.60"), response.getExpectedLoanRate());
        assertEquals(new BigDecimal("300000000"), response.getTotalRentalDeposit());
        assertEquals(new BigDecimal("270000000"), response.getLoanAmount());
        assertEquals(new BigDecimal("30000000"), response.getOwnFunds());

        assertEquals(new BigDecimal("1035000"), response.getMonthlyInterestCost());
        assertEquals(new BigDecimal("0"), response.getMonthlyRent());
        assertEquals(new BigDecimal("0"), response.getTotalLivingCost());

        assertEquals(new BigDecimal("270000"), response.getGuaranteeInsuranceFee());
        assertEquals(new BigDecimal("75000"), response.getStampDuty());
        assertEquals(3, response.getAvailableBanks().size());
    }

    @DisplayName("특정 대출추천 - 중기청(HF-01)")
    @Test
    void generateSpecificLoanAdvice_targetHF01() {
        // given
        LoanAdviceServiceRequest request = LoanAdviceServiceRequest.builder()
            .rentalDeposit(new BigDecimal("120000000"))
            .monthlyRent(new BigDecimal("0"))
            .cashOnHand(new BigDecimal("20000000"))
            .age(32)
            .maritalStatus(MaritalStatus.SINGLE)
            .annualIncome(new BigDecimal("45000000"))
            .spouseAnnualIncome(new BigDecimal("0"))
            .childStatus(ChildStatus.NO_CHILD)
            .hasNewborn(false)
            .houseOwnershipType(HouseOwnershipType.NO_HOUSE)
            .isSMEEmployee(true)
            .isNetAssetOver345M(false)
            .rentHousingType(RentHousingType.OFFICETEL)
            .exclusiveArea(new BigDecimal(84.5))
            .buildingName("행복아파트")
            .districtCode("1111011700")
            .dongName("역삼동")
            .jibun("649-5")
            .build();

        // when
        LoanAdviceResponse response = loanAdviceService.createLoanAdvice(request);

        // then
        assertNotNull(response);
        assertEquals("NHUF-03", response.getLoanProductCode());
        assertEquals("중소기업취업청년전월세대출", response.getLoanProductName());

        assertEquals(new BigDecimal("96000000"), response.getPossibleLoanLimit());
        assertEquals(new BigDecimal("1.5"), response.getExpectedLoanRate());
        assertEquals(new BigDecimal("120000000"), response.getTotalRentalDeposit());
        assertEquals(new BigDecimal("96000000"), response.getLoanAmount());
        assertEquals(new BigDecimal("24000000"), response.getOwnFunds());

        assertEquals(new BigDecimal("120000"), response.getMonthlyInterestCost());
        assertEquals(new BigDecimal("0"), response.getMonthlyRent());
        assertEquals(new BigDecimal("0"), response.getTotalLivingCost());

        assertEquals(new BigDecimal("295680"), response.getGuaranteeInsuranceFee());
        assertEquals(new BigDecimal("35000"), response.getStampDuty());
        assertEquals(7, response.getAvailableBanks().size());

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