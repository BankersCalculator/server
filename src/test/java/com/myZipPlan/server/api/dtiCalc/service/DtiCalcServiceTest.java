package com.myZipPlan.server.api.dtiCalc.service;

import com.myZipPlan.server.calculator.dtiCalc.dto.DtiCalcResponse;
import com.myZipPlan.server.calculator.dtiCalc.dto.DtiCalcServiceRequest;
import com.myZipPlan.server.calculator.dtiCalc.service.DtiCalcService;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
public class DtiCalcServiceTest {

    @Autowired
    private DtiCalcService dtiCalcService;

    private static final double DELTA = 0.001;

    @DisplayName("DTI 정상 산출 테스트")
    @MethodSource("provideTestCases")
    @ParameterizedTest
    void calculateDti(BigDecimal annualIncome, BigDecimal loanAmount,
                      BigDecimal interestRate, Integer loanTerm,
                      RepaymentType repaymentType, BigDecimal yearlyLoanInterestRepayment,
                      BigDecimal expectedDtiRatio, BigDecimal expectedAnnualRepaymentAmount)
        throws Exception {

        DtiCalcServiceRequest request = createTestRequest(annualIncome, loanAmount, interestRate,
            loanTerm, repaymentType, yearlyLoanInterestRepayment);

        DtiCalcResponse response = dtiCalcService.dtiCalculate(request);

        assertCommonExpectations(response, request, expectedDtiRatio, expectedAnnualRepaymentAmount);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
            Arguments.of(
                new BigDecimal("100000000"), new BigDecimal("300000000"), new BigDecimal("0.035"),
                360, RepaymentType.AMORTIZING, new BigDecimal("0"),
                new BigDecimal("0.1617"), new BigDecimal("16165609")
            ),
            Arguments.of(
                new BigDecimal("50000000"), new BigDecimal("400000000"), new BigDecimal("0.05"),
                240, RepaymentType.EQUAL_PRINCIPAL, new BigDecimal("0"),
                new BigDecimal("0.6008"), new BigDecimal("30041667")
            ),
            Arguments.of(
                new BigDecimal("50000000"), new BigDecimal("400000000"), new BigDecimal("0.05"),
                240, RepaymentType.EQUAL_PRINCIPAL, new BigDecimal("10000000"),
                new BigDecimal("0.8008"), new BigDecimal("40041667")
            ),
            Arguments.of(
                new BigDecimal("150000000"), new BigDecimal("1000000000"), new BigDecimal("0.045"),
                360, RepaymentType.BULLET, new BigDecimal("0"),
                new BigDecimal("0.5222"), new BigDecimal("78333333")
            )
        );
    }

    private DtiCalcServiceRequest createTestRequest(BigDecimal annualIncome, BigDecimal loanAmount,
                                                    BigDecimal interestRate, Integer loanTerm,
                                                    RepaymentType repaymentType, BigDecimal yearlyLoanInterestRepayment) {
        return DtiCalcServiceRequest.builder()
            .annualIncome(annualIncome)
            .loanAmount(loanAmount)
            .interestRate(interestRate)
            .loanTerm(loanTerm)
            .repaymentType(repaymentType)
            .yearlyLoanInterestRepayment(yearlyLoanInterestRepayment)
            .build();
    }

    private void assertCommonExpectations(DtiCalcResponse response, DtiCalcServiceRequest request,
                                          BigDecimal expectedDtiRatio, BigDecimal expectedAnnualRepaymentAmount) {
        log.info(request.getAnnualIncome().toString());
        log.info(response.getAnnualIncome().toString());
        log.info(response.getDtiRatio().toString());
        log.info(expectedDtiRatio.toString());
        log.info(response.getAnnualRepaymentAmount().toString());
        log.info(expectedAnnualRepaymentAmount.toString());

        assertEquals(0, request.getAnnualIncome().compareTo(response.getAnnualIncome()),
            "연간 소득이 일치하지 않습니다.");
        assertEquals(0, expectedDtiRatio.compareTo(response.getDtiRatio()),
            "DTI 비율이 일치하지 않습니다.");
        assertEquals(0, expectedAnnualRepaymentAmount.compareTo(response.getAnnualRepaymentAmount()),
            "연간 상환액이 일치하지 않습니다.");
    }
}
