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

    @DisplayName("DSR 정상 산출 - 모든 대출 유형")
    @MethodSource("provideTestCases")
    @ParameterizedTest
    void calculateDsrForAllLoanTypes(LoanType loanType, RepaymentType repaymentType,
                                     double principal, int term, double interestRate,
                                     double expectedDsrRatio) throws Exception {
        // given
        DsrCalcServiceRequest request = createTestRequest(loanType, repaymentType, principal, term, interestRate);

        // when
        DsrCalcResponse response = dsrCalcService.dsrCalculate(request);

        // then
        assertCommonExpectations(response, request, expectedDsrRatio);
//        assertSpecificExpectations(response, loanType, repaymentType);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
            Arguments.of(LoanType.DEPOSIT_AND_INSURANCE_COLLATERAL_LOAN, RepaymentType.BULLET, 200000000, 240, 0.03, 0.06),
            Arguments.of(LoanType.INTERIM_PAYMENT_AND_MOVING, RepaymentType.BULLET, 200000000, 240, 0.03, 0.14),
            Arguments.of(LoanType.JEONSE_DEPOSIT_COLLATERAL_LOAN, RepaymentType.BULLET, 200000000, 240, 0.03, 0.56),
            Arguments.of(LoanType.JEONSE_LOAN, RepaymentType.BULLET, 200000000, 240, 0.03, 0.06),
            Arguments.of(LoanType.LONG_TERM_CARD_LOAN, RepaymentType.BULLET, 200000000, 240, 0.03, 0.72),
            Arguments.of(LoanType.MORTGAGE, RepaymentType.BULLET, 200000000, 240, 0.03, 0.26),
            Arguments.of(LoanType.NON_HOUSING_REAL_ESTATE_COLLATERAL_LOAN, RepaymentType.BULLET, 200000000, 240, 0.03, 0.31),
            Arguments.of(LoanType.OFFICETEL_MORTGAGE_LOAN, RepaymentType.BULLET, 200000000, 240, 0.03, 0.31),
            Arguments.of(LoanType.OTHER_COLLATERAL_LOAN, RepaymentType.BULLET, 200000000, 240, 0.03, 0.26),
            Arguments.of(LoanType.OTHER_LOAN, RepaymentType.BULLET, 200000000, 240, 0.03, 0.16),
            Arguments.of(LoanType.PERSONAL_LOAN, RepaymentType.BULLET, 200000000, 240, 0.03, 0.46),
            Arguments.of(LoanType.SECURITIES_COLLATERAL_LOAN, RepaymentType.BULLET, 200000000, 240, 0.03, 0.31)
        );
    }

    private DsrCalcServiceRequest createTestRequest(LoanType loanType, RepaymentType repaymentType,
                                                    double principal, int term, double interestRate) {
        return DsrCalcServiceRequest.builder()
            .annualIncome(100000000)
            .loanStatusList(List.of(
                DsrCalcServiceRequest.LoanStatus.builder()
                    .repaymentType(repaymentType)
                    .loanType(loanType)
                    .principal(principal)
                    .term(term)
                    .gracePeriod(0)
                    .remainingTerm(0)
                    .interestRate(interestRate)
                    .build()
            ))
            .build();
    }

    private void assertCommonExpectations(DsrCalcResponse response, DsrCalcServiceRequest request, double expectedDsrRatio) {
        assertEquals(request.getAnnualIncome(), response.getAnnualIncome());
        assertEquals(request.getLoanStatusList().size(), response.getTotalLoanCount());
        assertEquals(expectedDsrRatio, response.getFinalDsrRatio(), 0.01);
    }

//    private void assertSpecificExpectations(DsrCalcResponse response, LoanType loanType, RepaymentType repaymentType) {
//
//    }
}