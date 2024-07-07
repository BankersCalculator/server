package com.bankersCalculator.bankersCalculator.dsrCalc.service;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.common.enums.RepaymentType;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcResponse;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
class DsrCalcServiceTest {

    @Autowired
    private DsrCalcService dsrCalcService;

    @DisplayName("DSR 정상 산출 - 신용대출/일시상환")
    @Test
    void calculateDsrForPersonalLoanWithBullet() throws Exception {

        double delta = 1;

        int annualIncome = 100000000;
        double principal = 100000000;
        int term = 60;
        int gracePeriod = 0;
        int remainingTerm = 0;
        double interestRate = 0.05;
        double expectedDsrRatio = 0.025;

        //given
        DsrCalcServiceRequest request = DsrCalcServiceRequest.builder()
            .annualIncome(annualIncome)
            .loanStatusList(List.of(
                DsrCalcServiceRequest.LoanStatus.builder()
                    .repaymentType(RepaymentType.BULLET)
                    .loanType(LoanType.PERSONAL_LOAN)
                    .principal(principal)
                    .term(term)
                    .gracePeriod(gracePeriod)
                    .remainingTerm(remainingTerm)
                    .interestRate(interestRate)
                    .build()
            ))
            .build();

        //when
        DsrCalcResponse response = dsrCalcService.dsrCalculate(request);

        //then
        // 소득, 대출건수 검증
        assertEquals(annualIncome, response.getAnnualIncome());
        assertEquals(request.getLoanStatusList().size(), response.getTotalLoanCount());

        // DSR 검증
        assertEquals(expectedDsrRatio, response.getFinalDsrRatio(), delta);

    }
}