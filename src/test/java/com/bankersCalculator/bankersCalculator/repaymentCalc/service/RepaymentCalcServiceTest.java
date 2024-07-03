package com.bankersCalculator.bankersCalculator.repaymentCalc.service;

import com.bankersCalculator.bankersCalculator.repaymentCalc.dto.RepaymentCalcDto;
import com.bankersCalculator.bankersCalculator.repaymentCalc.dto.RepaymentSchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RepaymentCalcServiceTest {

    @Autowired
    RepaymentCalcService repaymentCalcService;

    @DisplayName("정상 산출 테스트")
    @Test
    void calculateBulletLoanRepayment() throws Exception {
        //given

        double principal = 100000000;
        int term = 36;
        int gracePeriod = 0;
        double interestRate = 0.036;
        RepaymentCalcDto repaymentCalcDto = RepaymentCalcDto.builder()
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRate)
            .build();

        //when
        List<RepaymentSchedule> repaymentScheduleList = repaymentCalcService.calculateBulletLoanRepayment(repaymentCalcDto);

        //then
        double expectedMonthlyInterest = principal * interestRate / 12;

        for (int i = 0; i < repaymentScheduleList.size(); i++) {
            RepaymentSchedule schedule = repaymentScheduleList.get(i);

            assertEquals(i + 1, schedule.getInstallmentNumber());
            assertEquals(expectedMonthlyInterest, schedule.getInterestPayment(), 0.01);

            if (i == 35) {
                assertEquals(principal, schedule.getPrincipalPayment(), 0.01);
                assertEquals(expectedMonthlyInterest + principal, schedule.getTotalPayment(), 0.01);
                assertEquals(0.0, schedule.getRemainingPrinciple(), 0.01);
            } else {
                assertEquals(0, schedule.getPrincipalPayment(), 0.01);
                assertEquals(expectedMonthlyInterest, schedule.getTotalPayment(), 0.01);
                assertEquals(principal, schedule.getRemainingPrinciple(), 0.01);
            }

        }
    }

}