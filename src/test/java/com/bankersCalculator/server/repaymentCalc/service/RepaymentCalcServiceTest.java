package com.bankersCalculator.server.repaymentCalc.service;

import com.bankersCalculator.server.repaymentCalc.domain.RepaymentSchedule;
import com.bankersCalculator.server.repaymentCalc.dto.RepaymentCalcResponse;
import com.bankersCalculator.server.repaymentCalc.dto.RepaymentCalcServiceRequest;
import com.bankersCalculator.server.common.enums.RepaymentType;
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
        double delta = 0.001;

        double principal = 100000000;
        int term = 36;
        int gracePeriod = 0;
        double interestRate = 0.36;
        RepaymentCalcServiceRequest repaymentCalcServiceRequest = RepaymentCalcServiceRequest.builder()
            .repaymentType(RepaymentType.BULLET)
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRate)
            .build();

        //when
        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(repaymentCalcServiceRequest);
        List<RepaymentSchedule> repaymentScheduleList = repaymentCalcResponse.getRepaymentScheduleList();
        double totalPrincipal = repaymentCalcResponse.getTotalPrincipal();
        double totalInterest = repaymentCalcResponse.getTotalInterest();
        int totalInstallments = repaymentCalcResponse.getTotalInstallments();

        //then
        double expectedMonthlyInterest = principal * interestRate / 12;

        assertEquals(36, repaymentScheduleList.size());
        assertEquals(36, totalInstallments);
        assertEquals(principal, totalPrincipal, delta);
        assertEquals(expectedMonthlyInterest * term, totalInterest, delta);

        for (int i = 0; i < repaymentScheduleList.size(); i++) {
            RepaymentSchedule schedule = repaymentScheduleList.get(i);
            // 회차 및 월이자비용
            assertEquals(i + 1, schedule.getInstallmentNumber());
            assertEquals(expectedMonthlyInterest, schedule.getInterestPayment(), delta);

            if (i == 35) {
                // 최종회차 상환금
                assertEquals(principal, schedule.getPrincipalPayment(), delta);
                assertEquals(expectedMonthlyInterest + principal, schedule.getTotalPayment(), delta);
                assertEquals(0.0, schedule.getRemainingPrinciple(), delta);
            } else {
                // 일반회차 상환금
                assertEquals(0, schedule.getPrincipalPayment(), delta);
                assertEquals(expectedMonthlyInterest, schedule.getTotalPayment(), delta);
                assertEquals(principal, schedule.getRemainingPrinciple(), delta);
            }
        }
    }

    @DisplayName("원리금상환 정상 산출 테스트 with 거치기간")
    @Test
    void calculateAmortizingLoanRepayment() throws Exception {
        //given
        double delta = 0.001;

        double principal = 1000000;
        int term = 12;
        int gracePeriod = 2;
        double interestRate = 0.12;
        RepaymentCalcServiceRequest repaymentCalcServiceRequest = RepaymentCalcServiceRequest.builder()
            .repaymentType(RepaymentType.AMORTIZING)
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRate)
            .build();

        //when
        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(repaymentCalcServiceRequest);
        List<RepaymentSchedule> repaymentScheduleList = repaymentCalcResponse.getRepaymentScheduleList();

        double totalPrincipal = repaymentCalcResponse.getTotalPrincipal();
        double totalInterest = repaymentCalcResponse.getTotalInterest();
        int totalInstallments = repaymentCalcResponse.getTotalInstallments();

        //then
        assertEquals(12, repaymentScheduleList.size());
        assertEquals(12, totalInstallments);
        assertEquals(principal, totalPrincipal, delta);

        // 거치기간 테스트
        assertEquals(0, repaymentScheduleList.get(0).getPrincipalPayment());
        assertEquals(0, repaymentScheduleList.get(1).getPrincipalPayment());
        assertEquals(10000, repaymentScheduleList.get(0).getInterestPayment());
        assertEquals(10000, repaymentScheduleList.get(1).getInterestPayment());

        // 월상환금 테스트
        assertTrue(repaymentScheduleList.get(2).getPrincipalPayment() > 0);
        double expectedMonthlyPayment = calculateMonthlyPayment(principal, interestRate / 12, term - gracePeriod);
        assertEquals(expectedMonthlyPayment, repaymentScheduleList.get(2).getTotalPayment(), delta);

        // 최종회차 잔금 테스트
        assertEquals(0, repaymentScheduleList.get(11).getRemainingPrinciple(), delta);

        // 매월 원리금 금액 테스트
        for (int i = gracePeriod; i < term; i++) {
            assertEquals(expectedMonthlyPayment, repaymentScheduleList.get(i).getTotalPayment(), delta);
        }

        // 총 상환액 테스트
        double totalPayments = repaymentScheduleList.stream().mapToDouble(RepaymentSchedule::getTotalPayment).sum();
        double expectedTotal = (expectedMonthlyPayment * 10) + (10000 * 2); // 10개월 원리금 + 2개월 이자
        assertEquals(expectedTotal, totalPayments, delta);

        // 총 이자 테스트
        double sumInterest = repaymentScheduleList.stream().mapToDouble(RepaymentSchedule::getInterestPayment).sum();
        double expectedTotalInterest = expectedTotal - 1000000.0; // 총상환액 - 원금
        assertEquals(expectedTotalInterest, sumInterest, delta);
        assertEquals(expectedTotalInterest, totalInterest, delta);
    }

    @DisplayName("원리금상환 정상 산출 테스트 without 거치기간")
    @Test
    void calculateAmortizingLoanRepaymentWithoutGracePeriod() throws Exception {
        //given
        double delta = 1;

        double principal = 1000000;
        int term = 12;
        int gracePeriod = 0;
        double interestRate = 0.12;
        RepaymentCalcServiceRequest repaymentCalcServiceRequest = RepaymentCalcServiceRequest.builder()
            .repaymentType(RepaymentType.AMORTIZING)
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRate)
            .build();

        //when
        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(repaymentCalcServiceRequest);
        List<RepaymentSchedule> repaymentScheduleList = repaymentCalcResponse.getRepaymentScheduleList();

        double totalPrincipal = repaymentCalcResponse.getTotalPrincipal();
        double totalInterest = repaymentCalcResponse.getTotalInterest();
        int totalInstallments = repaymentCalcResponse.getTotalInstallments();

        //then
        assertEquals(12, repaymentScheduleList.size());
        assertEquals(12, totalInstallments);
        assertEquals(principal, totalPrincipal, delta);

        // 거치기간 테스트
        assertEquals(78849, repaymentScheduleList.get(0).getPrincipalPayment(), delta);
        assertEquals(79637, repaymentScheduleList.get(1).getPrincipalPayment(), delta);
        assertEquals(10000, repaymentScheduleList.get(0).getInterestPayment(), delta);
        assertEquals(9212, repaymentScheduleList.get(1).getInterestPayment(), delta);

        // 월상환금 테스트
        assertTrue(repaymentScheduleList.get(2).getPrincipalPayment() > 0);
        double expectedMonthlyPayment = calculateMonthlyPayment(principal, interestRate / 12, term - gracePeriod);
        assertEquals(expectedMonthlyPayment, repaymentScheduleList.get(2).getTotalPayment(), delta);

        // 최종회차 잔금 테스트
        assertEquals(0, repaymentScheduleList.get(11).getRemainingPrinciple(), delta);

        // 매월 원리금 금액 테스트
        for (int i = gracePeriod; i < term; i++) {
            assertEquals(expectedMonthlyPayment, repaymentScheduleList.get(i).getTotalPayment(), delta);
        }

        // 총 상환액 테스트
        double totalPayments = repaymentScheduleList.stream().mapToDouble(RepaymentSchedule::getTotalPayment).sum();
        double expectedTotal = (expectedMonthlyPayment * 12); // 12개월 원리금
        assertEquals(expectedTotal, totalPayments, delta);


        // 총 이자 테스트
        double sumInterest = repaymentScheduleList.stream().mapToDouble(RepaymentSchedule::getInterestPayment).sum();
        double expectedTotalInterest = expectedTotal - 1000000.0; // 총상환액 - 원금
        assertEquals(expectedTotalInterest, sumInterest, delta);
        assertEquals(expectedTotalInterest, totalInterest, delta);
    }

    @DisplayName("원금상환 정상 산출 테스트 with 거치기간")
    @Test
    void calculateEqualPrincipalLoanRepayment() throws Exception {
        //given
        double delta = 1;

        double principal = 1000000;
        int term = 12;
        int gracePeriod = 2;
        double interestRate = 0.12;
        RepaymentCalcServiceRequest repaymentCalcServiceRequest = RepaymentCalcServiceRequest.builder()
            .repaymentType(RepaymentType.EQUAL_PRINCIPAL)
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRate)
            .build();

        //when
        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(repaymentCalcServiceRequest);
        List<RepaymentSchedule> repaymentScheduleList = repaymentCalcResponse.getRepaymentScheduleList();

        double totalPrincipal = repaymentCalcResponse.getTotalPrincipal();
        double totalInterest = repaymentCalcResponse.getTotalInterest();
        int totalInstallments = repaymentCalcResponse.getTotalInstallments();

        //then
        assertEquals(12, repaymentScheduleList.size());
        assertEquals(12, totalInstallments);
        assertEquals(principal, totalPrincipal, delta);

        // 거치기간 테스트
        assertEquals(0, repaymentScheduleList.get(0).getPrincipalPayment(), delta);
        assertEquals(0, repaymentScheduleList.get(1).getPrincipalPayment(), delta);
        assertEquals(10000, repaymentScheduleList.get(0).getInterestPayment(), delta);
        assertEquals(10000, repaymentScheduleList.get(1).getInterestPayment(), delta);

        // 월상환 원금 테스트
        assertTrue(repaymentScheduleList.get(2).getPrincipalPayment() > 0);
        double expectedMonthlyPrincipalPayment = principal / (term - gracePeriod);
        for (int i = gracePeriod; i < term; i++) {
            assertEquals(expectedMonthlyPrincipalPayment, repaymentScheduleList.get(i).getPrincipalPayment(), delta);
        }

        // 총 이자 테스트
        double sumInterest = repaymentScheduleList.stream().mapToDouble(RepaymentSchedule::getInterestPayment).sum();
        double expectedTotalInterest = 0;
        double monthlyInterestRate = interestRate / 12;
        double remainingPricipal = principal;

        for (int i = 0; i < gracePeriod; i++) {
            expectedTotalInterest += remainingPricipal * monthlyInterestRate;
        }

        for (int i = gracePeriod; i < term; i++) {
            expectedTotalInterest += remainingPricipal * monthlyInterestRate;
            remainingPricipal -= expectedMonthlyPrincipalPayment;

        }
        assertEquals(expectedTotalInterest, sumInterest, delta);
        assertEquals(expectedTotalInterest, totalInterest, delta);

        // 최종회차 잔금 테스트
        assertEquals(0, repaymentScheduleList.get(11).getRemainingPrinciple(), delta);

        // 총 상환액 테스트
        double totalPayments = repaymentScheduleList.stream().mapToDouble(RepaymentSchedule::getTotalPayment).sum();
        double expectedTotal = expectedMonthlyPrincipalPayment * (term - gracePeriod) + expectedTotalInterest;
        assertEquals(expectedTotal, totalPayments, delta);
    }

    @DisplayName("원금상환 정상 산출 테스트 without 거치기간")
    @Test
    void calculateEqualPrincipalLoanRepaymentWithoutGracePeriod() throws Exception {
        //given
        double delta = 1;

        double principal = 1200000;
        int term = 12;
        int gracePeriod = 0;
        double interestRate = 0.12;
        RepaymentCalcServiceRequest repaymentCalcServiceRequest = RepaymentCalcServiceRequest.builder()
            .repaymentType(RepaymentType.EQUAL_PRINCIPAL)
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRate)
            .build();

        //when
        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(repaymentCalcServiceRequest);
        List<RepaymentSchedule> repaymentScheduleList = repaymentCalcResponse.getRepaymentScheduleList();

        double totalPrincipal = repaymentCalcResponse.getTotalPrincipal();
        double totalInterest = repaymentCalcResponse.getTotalInterest();
        int totalInstallments = repaymentCalcResponse.getTotalInstallments();

        //then
        assertEquals(12, repaymentScheduleList.size());
        assertEquals(12, totalInstallments);
        assertEquals(principal, totalPrincipal, delta);

        // 거치기간 테스트
        assertEquals(100000, repaymentScheduleList.get(0).getPrincipalPayment(), delta);
        assertEquals(100000, repaymentScheduleList.get(1).getPrincipalPayment(), delta);
        assertEquals(12000, repaymentScheduleList.get(0).getInterestPayment(), delta);
        assertEquals(11000, repaymentScheduleList.get(1).getInterestPayment(), delta);

        // 월상환 원금 테스트
        assertTrue(repaymentScheduleList.get(2).getPrincipalPayment() > 0);
        double expectedMonthlyPrincipalPayment = principal / (term - gracePeriod);
        for (int i = gracePeriod; i < term; i++) {
            assertEquals(expectedMonthlyPrincipalPayment, repaymentScheduleList.get(i).getPrincipalPayment(), delta);
        }

        // 총 이자 테스트
        double sumInterest = repaymentScheduleList.stream().mapToDouble(RepaymentSchedule::getInterestPayment).sum();
        double expectedTotalInterest = 0;
        double monthlyInterestRate = interestRate / 12;
        double remainingPricipal = principal;

        for (int i = 0; i < gracePeriod; i++) {
            expectedTotalInterest += remainingPricipal * monthlyInterestRate;
        }

        for (int i = gracePeriod; i < term; i++) {
            expectedTotalInterest += remainingPricipal * monthlyInterestRate;
            remainingPricipal -= expectedMonthlyPrincipalPayment;

        }
        assertEquals(expectedTotalInterest, sumInterest, delta);
        assertEquals(expectedTotalInterest, totalInterest, delta);

        // 최종회차 잔금 테스트
        assertEquals(0, repaymentScheduleList.get(11).getRemainingPrinciple(), delta);

        // 총 상환액 테스트
        double totalPayments = repaymentScheduleList.stream().mapToDouble(RepaymentSchedule::getTotalPayment).sum();
        double expectedTotal = expectedMonthlyPrincipalPayment * (term - gracePeriod) + expectedTotalInterest;
        assertEquals(expectedTotal, totalPayments, delta);
    }

    private double calculateMonthlyPayment(double principal, double monthlyInterestRate, int numberOfPayments) {
        return principal * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfPayments))
            / (Math.pow(1 + monthlyInterestRate, numberOfPayments) - 1);
    }


}