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

    @DisplayName("일시상환 정상 산출 테스트")
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

        assertEquals(36, repaymentScheduleList.size());

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

    @DisplayName("원리금상환 정상 산출 테스트")
    @Test
    void calculateAmortizingLoanRepayment() throws Exception {
        //given

        double principal = 1000000;
        int term = 12;
        int gracePeriod = 2;
        double interestRate = 0.12;
        RepaymentCalcDto repaymentCalcDto = RepaymentCalcDto.builder()
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRate)
            .build();

        //when
        List<RepaymentSchedule> repaymentScheduleList = repaymentCalcService.calculateAmortizingLoanRepayment(repaymentCalcDto);

        //then
        assertEquals(12, repaymentScheduleList.size());

        // 거치기간 테스트
        assertEquals(0, repaymentScheduleList.get(0).getPrincipalPayment());
        assertEquals(0, repaymentScheduleList.get(1).getPrincipalPayment());
        assertEquals(10000, repaymentScheduleList.get(0).getInterestPayment());
        assertEquals(10000, repaymentScheduleList.get(1).getInterestPayment());

        // 상환기간 테스트
        assertTrue(repaymentScheduleList.get(2).getPrincipalPayment() > 0);
        double expectedMonthlyPayment = calculateMonthlyPayment(principal, interestRate / 12, term - gracePeriod);
        assertEquals(expectedMonthlyPayment, repaymentScheduleList.get(2).getTotalPayment(), 0.01);

        // 최종회차 잔금 테스트
        assertEquals(0, repaymentScheduleList.get(11).getRemainingPrinciple(), 0.01);

        // 매월 원리금 금액 테스트
        for (int i = gracePeriod; i < term; i++) {
            assertEquals(expectedMonthlyPayment, repaymentScheduleList.get(i).getTotalPayment(), 0.01);
        }

        // 총 상환액 테스트
        double totalPayments = repaymentScheduleList.stream().mapToDouble(RepaymentSchedule::getTotalPayment).sum();
        double expectedTotal = (expectedMonthlyPayment * 10) + (10000 * 2); // 10개월 원리금 + 2개월 이자

        // 총 이자 테스트
        double totalInterest = repaymentScheduleList.stream().mapToDouble(RepaymentSchedule::getInterestPayment).sum();
        double expectedTotalInterest = expectedTotal - 1000000.0; // 총상환액 - 원금
        assertEquals(expectedTotalInterest, totalInterest, 0.01);


    }

    private double calculateMonthlyPayment(double principal, double monthlyInterestRate, int numberOfPayments) {
        return principal * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfPayments))
            / (Math.pow(1 + monthlyInterestRate, numberOfPayments) - 1);
    }

}