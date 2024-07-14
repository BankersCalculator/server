package com.bankersCalculator.bankersCalculator.dsrCalc.service;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.common.enums.RepaymentType;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcResponse;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
class DsrCalcServiceTest {

    @Autowired
    private DsrCalcService dsrCalcService;

    // DELTA 수준에 따라 검증의 정확도가 달라진다.
    private final double DELTA = 0.01;

    @DisplayName("DSR 정상 산출 - 모든 대출 유형")
    @MethodSource("provideTestCases")
    @ParameterizedTest
    void calculateDsrForAllLoanTypes(LoanType loanType, RepaymentType repaymentType,
                                     double principal, double maturityPaymentAmount,
                                     int term, int gracePeriod, double interestRate,
                                     double expectedDsrRatio) throws Exception {
        // given
        DsrCalcServiceRequest request = createTestRequest(loanType, repaymentType, principal, term, gracePeriod, interestRate, maturityPaymentAmount);

        // when
        DsrCalcResponse response = dsrCalcService.dsrCalculate(request);

        // then
        assertCommonExpectations(response, request, expectedDsrRatio);
//        assertSpecificExpectations(response, loanType, repaymentType);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
            Arguments.of(LoanType.DEPOSIT_AND_INSURANCE_COLLATERAL_LOAN, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.06),
            Arguments.of(LoanType.INTERIM_PAYMENT_AND_MOVING, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.14),
            Arguments.of(LoanType.JEONSE_DEPOSIT_COLLATERAL_LOAN, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.56),
            Arguments.of(LoanType.JEONSE_LOAN, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.06),
            Arguments.of(LoanType.LONG_TERM_CARD_LOAN, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.72),
            Arguments.of(LoanType.MORTGAGE, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.26),
            Arguments.of(LoanType.NON_HOUSING_REAL_ESTATE_COLLATERAL_LOAN, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.31),
            Arguments.of(LoanType.OFFICETEL_MORTGAGE_LOAN, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.31),
            Arguments.of(LoanType.OTHER_COLLATERAL_LOAN, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.26),
            Arguments.of(LoanType.OTHER_LOAN, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.16),
            Arguments.of(LoanType.PERSONAL_LOAN, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.46),
            Arguments.of(LoanType.SECURITIES_COLLATERAL_LOAN, RepaymentType.BULLET, 200000000, 0, 240, 0, 0.03, 0.31),

            Arguments.of(LoanType.DEPOSIT_AND_INSURANCE_COLLATERAL_LOAN, RepaymentType.AMORTIZING, 200000000, 50000000, 240, 60, 0.03, 0.06),
            Arguments.of(LoanType.INTERIM_PAYMENT_AND_MOVING, RepaymentType.AMORTIZING, 200000000, 50000000, 240, 60, 0.03, 0.1193),
            Arguments.of(LoanType.JEONSE_DEPOSIT_COLLATERAL_LOAN, RepaymentType.AMORTIZING, 200000000, 50000000, 240, 60, 0.03, 0.5393),
            Arguments.of(LoanType.JEONSE_LOAN, RepaymentType.AMORTIZING, 200000000, 50000000, 240, 60, 0.03, 0.06),
            Arguments.of(LoanType.LONG_TERM_CARD_LOAN, RepaymentType.AMORTIZING, 200000000, 50000000, 240, 60, 0.03, 0.4393),
            Arguments.of(LoanType.MORTGAGE, RepaymentType.AMORTIZING, 200000000, 50000000, 240, 60, 0.03, 0.1170),
            Arguments.of(LoanType.NON_HOUSING_REAL_ESTATE_COLLATERAL_LOAN, RepaymentType.AMORTIZING, 200000000, 50000000, 240, 60, 0.03, 0.2893),
            Arguments.of(LoanType.OFFICETEL_MORTGAGE_LOAN, RepaymentType.AMORTIZING, 200000000, 50000000, 240, 60, 0.03, 0.1170),
            Arguments.of(LoanType.OTHER_COLLATERAL_LOAN, RepaymentType.AMORTIZING, 200000000, 50000000, 240, 60, 0.03, 0.2393),
            Arguments.of(LoanType.OTHER_LOAN, RepaymentType.AMORTIZING, 200000000, 50000000, 240, 60, 0.03, 0.1393),
            Arguments.of(LoanType.PERSONAL_LOAN, RepaymentType.AMORTIZING, 200000000, 50000000, 240, 60, 0.03, 0.2393),
            Arguments.of(LoanType.SECURITIES_COLLATERAL_LOAN, RepaymentType.AMORTIZING, 200000000, 50000000, 240, 60, 0.03, 0.2893),
            // 네이버 DSR 계산기와 비교
            Arguments.of(LoanType.MORTGAGE, RepaymentType.AMORTIZING, 200000000, 0, 360, 0, 0.05, 0.1288),
            Arguments.of(LoanType.MORTGAGE, RepaymentType.AMORTIZING, 300000000, 0, 480, 0, 0.05, 0.1736),
            Arguments.of(LoanType.MORTGAGE, RepaymentType.AMORTIZING, 100000000, 0, 120, 0, 0.1, 0.1586),

            Arguments.of(LoanType.DEPOSIT_AND_INSURANCE_COLLATERAL_LOAN, RepaymentType.EQUAL_PRINCIPAL, 200000000, 50000000, 240, 60, 0.03, 0.06),
            Arguments.of(LoanType.INTERIM_PAYMENT_AND_MOVING, RepaymentType.EQUAL_PRINCIPAL, 200000000, 50000000, 240, 60, 0.03, 0.1176),
            Arguments.of(LoanType.JEONSE_DEPOSIT_COLLATERAL_LOAN, RepaymentType.EQUAL_PRINCIPAL, 200000000, 50000000, 240, 60, 0.03, 0.5376),
            Arguments.of(LoanType.JEONSE_LOAN, RepaymentType.EQUAL_PRINCIPAL, 200000000, 50000000, 240, 60, 0.03, 0.06),
            Arguments.of(LoanType.LONG_TERM_CARD_LOAN, RepaymentType.EQUAL_PRINCIPAL, 200000000, 50000000, 240, 60, 0.03, 0.4376),
            Arguments.of(LoanType.MORTGAGE, RepaymentType.EQUAL_PRINCIPAL, 200000000, 50000000, 240, 60, 0.03, 0.1154),
            Arguments.of(LoanType.NON_HOUSING_REAL_ESTATE_COLLATERAL_LOAN, RepaymentType.EQUAL_PRINCIPAL, 200000000, 50000000, 240, 60, 0.03, 0.2876),
            Arguments.of(LoanType.OFFICETEL_MORTGAGE_LOAN, RepaymentType.EQUAL_PRINCIPAL, 200000000, 50000000, 240, 60, 0.03, 0.1154),
            Arguments.of(LoanType.OTHER_COLLATERAL_LOAN, RepaymentType.EQUAL_PRINCIPAL, 200000000, 50000000, 240, 60, 0.03, 0.2376),
            Arguments.of(LoanType.OTHER_LOAN, RepaymentType.EQUAL_PRINCIPAL, 200000000, 50000000, 240, 60, 0.03, 0.1376),
            Arguments.of(LoanType.PERSONAL_LOAN, RepaymentType.EQUAL_PRINCIPAL, 200000000, 50000000, 240, 60, 0.03, 0.2376),
            Arguments.of(LoanType.SECURITIES_COLLATERAL_LOAN, RepaymentType.EQUAL_PRINCIPAL, 200000000, 50000000, 240, 60, 0.03, 0.2876),
            // 네이버 DSR 계산기와 비교
            Arguments.of(LoanType.MORTGAGE, RepaymentType.EQUAL_PRINCIPAL, 200000000, 0, 360, 0, 0.05, 0.1168),
            Arguments.of(LoanType.MORTGAGE, RepaymentType.EQUAL_PRINCIPAL, 300000000, 0, 480, 0, 0.05, 0.1502),
            Arguments.of(LoanType.MORTGAGE, RepaymentType.EQUAL_PRINCIPAL, 100000000, 0, 120, 0, 0.1, 0.1504)



        );
    }

    private DsrCalcServiceRequest createTestRequest(LoanType loanType, RepaymentType repaymentType,
                                                    double principal, int term, int gracePeriod,
                                                    double interestRate, double maturityPaymentAmount) {
        return DsrCalcServiceRequest.builder()
            .annualIncome(100000000)
            .loanStatusList(List.of(
                DsrCalcServiceRequest.LoanStatus.builder()
                    .repaymentType(repaymentType)
                    .loanType(loanType)
                    .principal(principal)
                    .term(term)
                    .gracePeriod(gracePeriod)
                    .interestRate(interestRate)
                    .maturityPaymentAmount(maturityPaymentAmount)
                    .build()
            ))
            .build();
    }

    private void assertCommonExpectations(DsrCalcResponse response, DsrCalcServiceRequest request, double expectedDsrRatio) {
        assertEquals(request.getAnnualIncome(), response.getAnnualIncome());
        assertEquals(request.getLoanStatusList().size(), response.getTotalLoanCount());
        assertEquals(expectedDsrRatio, response.getFinalDsrRatio(), DELTA);
    }
}