package com.myZipPlan.server.api.ltvCalc.service;

import com.myZipPlan.server.IntegrationTestSupport;
import com.myZipPlan.server.calculator.ltvCalc.dto.LtvCalcResponse;
import com.myZipPlan.server.calculator.ltvCalc.dto.LtvCalcRequest;
import com.myZipPlan.server.calculator.ltvCalc.service.LtvCalcService;
import com.myZipPlan.server.common.enums.calculator.LoanPurpose;
import com.myZipPlan.server.common.enums.calculator.RegionType;
import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class LtvCalcServiceTest extends IntegrationTestSupport {

    @Autowired
    private LtvCalcService ltvCalcService;

    private static final double DELTA = 0.001;

    @DisplayName("LTV 정상 산출")
    @MethodSource("provideTestCases")
    @ParameterizedTest
    void calculateLtv(LoanPurpose loanPurpose, BigDecimal collateralValue, RegionType regionType,
                      HouseOwnershipType houseOwnershipType, BigDecimal expectedLtvRatio,
                      BigDecimal expectedPossibleLoanAmount) throws Exception {
        // given
        LtvCalcRequest request = createTestRequest(loanPurpose, collateralValue, regionType, houseOwnershipType);

        // when
        LtvCalcResponse response = ltvCalcService.calculate(request.toServiceRequest());

        // then
        assertCommonExpectations(response, request, expectedLtvRatio, expectedPossibleLoanAmount);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
            // 주택구입자금
            Arguments.of(LoanPurpose.HOME_PURCHASE, new BigDecimal("500000000"),
                RegionType.REGULATED_AREA, HouseOwnershipType.LIFETIME_FIRST,
                new BigDecimal("0.8"), new BigDecimal("400000000")),
            Arguments.of(LoanPurpose.HOME_PURCHASE, new BigDecimal("500000000"),
                RegionType.NON_REGULATED_CAPITAL_AREA, HouseOwnershipType.NO_HOUSE,
                new BigDecimal("0.7"), new BigDecimal("350000000")),
            Arguments.of(LoanPurpose.HOME_PURCHASE, new BigDecimal("500000000"),
                RegionType.OTHER_AREAS, HouseOwnershipType.SINGLE_HOUSE_DISPOSAL,
                new BigDecimal("0.7"), new BigDecimal("350000000")),
            // 생활안정자금
            Arguments.of(LoanPurpose.LIVING_STABILITY, new BigDecimal("500000000"),
                RegionType.OTHER_AREAS, HouseOwnershipType.MORE_THAN_TWO_HOUSE,
                new BigDecimal("0.6"), new BigDecimal("200000000")),
            Arguments.of(LoanPurpose.LIVING_STABILITY, new BigDecimal("100000000"),
                RegionType.OTHER_AREAS, HouseOwnershipType.MORE_THAN_TWO_HOUSE,
                new BigDecimal("0.6"), new BigDecimal("60000000")),
            Arguments.of(LoanPurpose.LIVING_STABILITY, new BigDecimal("300000000"),
                RegionType.REGULATED_AREA, HouseOwnershipType.MORE_THAN_TWO_HOUSE,
                new BigDecimal("0.4"), new BigDecimal("120000000"))

        );
    }

    private LtvCalcRequest createTestRequest(LoanPurpose loanPurpose, BigDecimal collateralValue,
                                             RegionType regionType, HouseOwnershipType houseOwnershipType) {
        return LtvCalcRequest.builder()
            .loanPurpose(loanPurpose)
            .collateralValue(collateralValue)
            .regionType(regionType)
            .houseOwnershipType(houseOwnershipType)
            .build();
    }

    private void assertCommonExpectations(LtvCalcResponse response, LtvCalcRequest request,
                                          BigDecimal expectedLtvRatio, BigDecimal expectedPossibleLoanAmount) {
        assertEquals(0, request.getCollateralValue().compareTo(response.getCollateralValue()),
            "담보가치가 일치하지 않습니다.");
        assertEquals(0, expectedLtvRatio.compareTo(response.getLtvRatio()), "LTV 비율이 일치하지 않습니다.");
        assertEquals(0, expectedPossibleLoanAmount.compareTo(response.getPossibleLoanAmount()), "대출 가능 금액이 일치하지 않습니다.");
    }
}