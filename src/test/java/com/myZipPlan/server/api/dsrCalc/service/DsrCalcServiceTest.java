package com.myZipPlan.server.api.dsrCalc.service;

import com.myZipPlan.server.IntegrationTestSupport;
import com.myZipPlan.server.calculator.dsrCalc.dto.DsrCalcResponse;
import com.myZipPlan.server.calculator.dsrCalc.dto.DsrCalcServiceRequest;
import com.myZipPlan.server.calculator.dsrCalc.service.DsrCalcService;
import com.myZipPlan.server.common.enums.calculator.InterestRateType;
import com.myZipPlan.server.common.enums.calculator.LoanType;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class DsrCalcServiceTest extends IntegrationTestSupport {

    @Autowired
    private DsrCalcService dsrCalcService;

    // DELTA 수준에 따라 검증의 정확도가 달라진다.
    private final double DELTA = 0.001;

    @DisplayName("DSR 정상 산출 - 모든 대출 유형")
    @MethodSource("provideTestCases")
    @ParameterizedTest
    void calculateDsrForAllLoanTypes(LoanType loanType, RepaymentType repaymentType,
                                     double principal, double maturityPaymentAmount,
                                     int term, int gracePeriod, double interestRate,
                                     double expectedDsrRatio, Boolean isMetroArea,
                                     InterestRateType interestRateType) throws Exception {
        // given
        DsrCalcServiceRequest request = createTestRequest(loanType, repaymentType, principal, term, gracePeriod, interestRate, maturityPaymentAmount, isMetroArea, interestRateType);

        // when
        DsrCalcResponse response = dsrCalcService.dsrCalculate(request);

        // then
        assertCommonExpectations(response, request, expectedDsrRatio);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
            Arguments.of(LoanType.MORTGAGE, RepaymentType.BULLET, 200000000, 0, 360, 0, 0.03, 0.284, true, InterestRateType.VARIABLE),
            Arguments.of(LoanType.JEONSE_DEPOSIT_COLLATERAL_LOAN, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.5672, true, InterestRateType.PERIODIC),
            Arguments.of(LoanType.JEONSE_LOAN, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.075, false, InterestRateType.VARIABLE),
            Arguments.of(LoanType.LONG_TERM_CARD_LOAN, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.7411, true, InterestRateType.MIXED),

            Arguments.of(LoanType.MORTGAGE, RepaymentType.AMORTIZING, 200000000, 0, 240, 0, 0.03, 0.1345, false, InterestRateType.PERIODIC),
            Arguments.of(LoanType.MORTGAGE, RepaymentType.AMORTIZING, 200000000, 50000000, 240, 60, 0.03, 0.1187, false, InterestRateType.PERIODIC),
            Arguments.of(LoanType.PERSONAL_LOAN, RepaymentType.AMORTIZING, 200000000, 50000000, 240, 0, 0.03, 0.2419, true, InterestRateType.MIXED),
            // 네이버 DSR 계산기와 비교
            Arguments.of(LoanType.MORTGAGE, RepaymentType.EQUAL_PRINCIPAL, 200000000, 0, 360, 0, 0.05, 0.1232, false, InterestRateType.VARIABLE),
            Arguments.of(LoanType.MORTGAGE, RepaymentType.EQUAL_PRINCIPAL, 300000000, 0, 480, 0, 0.05, 0.1597, true, InterestRateType.MIXED),
            Arguments.of(LoanType.MORTGAGE, RepaymentType.AMORTIZING, 100000000, 0, 120, 0, 0.03, 0.1171, false, InterestRateType.PERIODIC)
        );
    }

    private DsrCalcServiceRequest createTestRequest(LoanType loanType, RepaymentType repaymentType,
                                                    double principal, int term, int gracePeriod,
                                                    double interestRate, double maturityPaymentAmount,
                                                    Boolean isMetroArea, InterestRateType interestRateType) {

        BigDecimal interestRateAddition = getInterestRateAddition(isMetroArea, interestRateType);
        interestRate += interestRateAddition.doubleValue();

        return DsrCalcServiceRequest.builder()
            .annualIncome(BigDecimal.valueOf(100000000))
            .loanStatusList(List.of(
                DsrCalcServiceRequest.LoanStatus.builder()
                    .repaymentType(repaymentType)
                    .loanType(loanType)
                    .principal(BigDecimal.valueOf(principal))
                    .term(BigDecimal.valueOf(term))
                    .gracePeriod(BigDecimal.valueOf(gracePeriod))
                    .interestRate(BigDecimal.valueOf(interestRate))
                    .maturityPaymentAmount(BigDecimal.valueOf(maturityPaymentAmount))
                    .isMetroArea(isMetroArea)
                    .interestRateType(interestRateType)
                    .build()
            ))
            .build();
    }

    private void assertCommonExpectations(DsrCalcResponse response, DsrCalcServiceRequest request, double expectedDsrRatio) {

        log.info("response.getFinalDsrRatio(): {}", response.getFinalDsrRatio());
        log.info("response.getFinalDsrRatio().doubleValue(): {}", response.getFinalDsrRatio().doubleValue());
        log.info("expectedDsrRatio: {}", expectedDsrRatio);

        assertEquals(0, request.getAnnualIncome().compareTo(response.getAnnualIncome()), DELTA);
        assertEquals(request.getLoanStatusList().size(), response.getTotalLoanCount().intValue());
        assertEquals(expectedDsrRatio, response.getFinalDsrRatio().doubleValue(), DELTA);
    }


    private BigDecimal getInterestRateAddition(Boolean isMetroArea, InterestRateType interestRateType) {
        if (isMetroArea) {
            return interestRateType.getMetroAreaInterestRateAddition();
        } else {
            return interestRateType.getNonMetroAreaInterestRateAddition();
        }

    }
}