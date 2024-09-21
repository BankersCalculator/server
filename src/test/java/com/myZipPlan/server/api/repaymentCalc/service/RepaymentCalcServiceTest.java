package com.myZipPlan.server.api.repaymentCalc.service;

import com.myZipPlan.server.IntegrationTestSupport;
import com.myZipPlan.server.calculator.repaymentCalc.domain.RepaymentSchedule;
import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcServiceRequest;
import com.myZipPlan.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class RepaymentCalcServiceTest extends IntegrationTestSupport {

    @Autowired
    RepaymentCalcService repaymentCalcService;

    @DisplayName("일시상환 정상 산출 테스트")
    @Test
    void calculateBulletLoanRepayment() throws Exception {
        //given
        double delta = 0.001;

        BigDecimal principal = BigDecimal.valueOf(100000000);
        BigDecimal term = BigDecimal.valueOf(36);
        BigDecimal gracePeriod = BigDecimal.valueOf(0);
        BigDecimal interestRate = BigDecimal.valueOf(0.36);
        RepaymentCalcServiceRequest repaymentCalcServiceRequest = RepaymentCalcServiceRequest.builder()
            .repaymentType(RepaymentType.BULLET)
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRate)
            .build();

        //when
        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculate(repaymentCalcServiceRequest);
        List<RepaymentSchedule> repaymentScheduleList = repaymentCalcResponse.getRepaymentSchedules();
        BigDecimal totalPrincipal = repaymentCalcResponse.getTotalPrincipal();
        BigDecimal totalInterest = repaymentCalcResponse.getTotalInterest();
        BigDecimal totalInstallments = repaymentCalcResponse.getTotalInstallments();

        //then
        BigDecimal expectedMonthlyInterest = principal.multiply(interestRate).divide(BigDecimal.valueOf(12), 4, RoundingMode.DOWN);

        assertEquals(36, repaymentScheduleList.size());
        assertEquals(BigDecimal.valueOf(36), totalInstallments);
        assertEquals(0, principal.compareTo(totalPrincipal));

        BigDecimal expectedTotalInterest = expectedMonthlyInterest.multiply(term);
        assertEquals(0, expectedTotalInterest.compareTo(totalInterest));

        for (int i = 0; i < repaymentScheduleList.size(); i++) {
            RepaymentSchedule schedule = repaymentScheduleList.get(i);
            // 회차 및 월이자비용
            assertEquals(BigDecimal.valueOf(i + 1), schedule.getInstallmentNumber());
            assertEquals(0, schedule.getInterestPayment().compareTo(expectedMonthlyInterest));
            if (i == 35) {
                // 최종회차 상환금
                assertEquals(0, schedule.getPrincipalPayment().compareTo(principal));
                assertEquals(0, schedule.getTotalPayment().compareTo(expectedMonthlyInterest.add(principal)));
                assertEquals(0, schedule.getRemainingPrincipal().compareTo(BigDecimal.ZERO));
            } else {
                // 일반회차 상환금
                assertEquals(0, schedule.getPrincipalPayment().compareTo(BigDecimal.ZERO));
                assertEquals(0, schedule.getTotalPayment().compareTo(expectedMonthlyInterest));
                assertEquals(0, schedule.getRemainingPrincipal().compareTo(principal));
            }

        }
    }

    @DisplayName("원리금상환 정상 산출 테스트 with 거치기간")
    @Test
    void calculateAmortizingLoanRepayment() throws Exception {
        //given
        double delta = 0.001;

        BigDecimal principal = BigDecimal.valueOf(1000000);
        BigDecimal term = BigDecimal.valueOf(12);
        BigDecimal gracePeriod = BigDecimal.valueOf(2);
        BigDecimal interestRate = BigDecimal.valueOf(0.12);
        RepaymentCalcServiceRequest repaymentCalcServiceRequest = RepaymentCalcServiceRequest.builder()
            .repaymentType(RepaymentType.AMORTIZING)
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRate)
            .build();

        //when
        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculate(repaymentCalcServiceRequest);
        List<RepaymentSchedule> repaymentScheduleList = repaymentCalcResponse.getRepaymentSchedules();

        BigDecimal totalPrincipal = repaymentCalcResponse.getTotalPrincipal();
        BigDecimal totalInterest = repaymentCalcResponse.getTotalInterest();
        BigDecimal totalInstallments = repaymentCalcResponse.getTotalInstallments();

        //then
        assertEquals(12, repaymentScheduleList.size());
        assertEquals(BigDecimal.valueOf(12), totalInstallments);
        assertEquals(0, principal.compareTo(totalPrincipal));

        // 거치기간 테스트
        assertEquals(BigDecimal.ZERO, repaymentScheduleList.get(0).getPrincipalPayment());
        assertEquals(BigDecimal.ZERO, repaymentScheduleList.get(1).getPrincipalPayment());
        assertEquals(BigDecimal.valueOf(10000), repaymentScheduleList.get(0).getInterestPayment());
        assertEquals(BigDecimal.valueOf(10000), repaymentScheduleList.get(1).getInterestPayment());

        // 월상환금 테스트

        assertTrue(repaymentScheduleList.get(2).getPrincipalPayment().compareTo(BigDecimal.ZERO) > 0, "원금 상환금이 0보다 큰지 확인");
        BigDecimal expectedMonthlyPayment = calculateMonthlyPayment(principal, interestRate.divide(new BigDecimal("12"), 4, RoundingMode.DOWN), term.subtract(gracePeriod));
        assertEquals(0, expectedMonthlyPayment.compareTo(repaymentScheduleList.get(2).getTotalPayment()));

        // 최종회차 잔금 테스트
        assertEquals(0, repaymentScheduleList.get(11).getRemainingPrincipal().compareTo(BigDecimal.ZERO));

        // 매월 원리금 금액 테스트
        for (int i = gracePeriod.intValue(); i < term.intValue(); i++) {
            assertEquals(0, expectedMonthlyPayment.compareTo(repaymentScheduleList.get(i).getTotalPayment()));
        }

        // 총 상환액 테스트
        BigDecimal totalPayments = repaymentScheduleList.stream()
            .map(RepaymentSchedule::getTotalPayment)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expectedTotal = expectedMonthlyPayment.multiply(BigDecimal.valueOf(10)).add(BigDecimal.valueOf(10000 * 2));
        assertEquals(0, expectedTotal.compareTo(totalPayments));

        // 총 이자 테스트
        BigDecimal sumInterest = repaymentScheduleList.stream()
            .map(RepaymentSchedule::getInterestPayment)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expectedTotalInterest = expectedTotal.subtract(principal);
        log.info("expectedTotalInterest: {}", expectedTotalInterest);
        log.info("sumInterest: {}", sumInterest);
        log.info("totalInterest: {}", totalInterest);
        assertEquals(0, expectedTotalInterest.compareTo(sumInterest));
        assertEquals(0, expectedTotalInterest.compareTo(totalInterest));
    }

    @DisplayName("원리금상환 정상 산출 테스트 without 거치기간")
    @Test
    void calculateAmortizingLoanRepaymentWithoutGracePeriod() throws Exception {
        //given
        BigDecimal principal = BigDecimal.valueOf(1000000);
        BigDecimal term = BigDecimal.valueOf(12);
        BigDecimal gracePeriod = BigDecimal.ZERO;
        BigDecimal interestRate = BigDecimal.valueOf(0.12);
        RepaymentCalcServiceRequest repaymentCalcServiceRequest = RepaymentCalcServiceRequest.builder()
            .repaymentType(RepaymentType.AMORTIZING)
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRate)
            .build();

        //when
        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculate(repaymentCalcServiceRequest);
        List<RepaymentSchedule> repaymentScheduleList = repaymentCalcResponse.getRepaymentSchedules();
        BigDecimal totalPrincipal = repaymentCalcResponse.getTotalPrincipal();
        BigDecimal totalInterest = repaymentCalcResponse.getTotalInterest();
        BigDecimal totalInstallments = repaymentCalcResponse.getTotalInstallments();

        //then
        assertEquals(12, repaymentScheduleList.size());
        assertEquals(BigDecimal.valueOf(12), totalInstallments);
        assertEquals(0, principal.compareTo(totalPrincipal));

        // 거치기간 테스트
        assertEquals(0, BigDecimal.valueOf(78849).compareTo(repaymentScheduleList.get(0).getPrincipalPayment()));
        assertEquals(0, BigDecimal.valueOf(79637).compareTo(repaymentScheduleList.get(1).getPrincipalPayment()));
        assertEquals(0, BigDecimal.valueOf(10000).compareTo(repaymentScheduleList.get(0).getInterestPayment()));
        assertEquals(0, BigDecimal.valueOf(9212).compareTo(repaymentScheduleList.get(1).getInterestPayment()));

        // 월상환금 테스트
        assertTrue(repaymentScheduleList.get(2).getPrincipalPayment().compareTo(BigDecimal.ZERO) > 0, "원금 상환금이 0보다 큰지 확인");
        BigDecimal expectedMonthlyPayment = calculateMonthlyPayment(principal, interestRate.divide(new BigDecimal("12"), 4, RoundingMode.DOWN), term.subtract(gracePeriod));
        assertEquals(0, expectedMonthlyPayment.compareTo(repaymentScheduleList.get(2).getTotalPayment()));

        // 최종회차 잔금 테스트
        assertEquals(0, repaymentScheduleList.get(11).getRemainingPrincipal().compareTo(BigDecimal.ZERO));

        // 매월 원리금 금액 테스트
        for (int i = gracePeriod.intValue(); i < term.intValue(); i++) {
            assertEquals(0, expectedMonthlyPayment.compareTo(repaymentScheduleList.get(i).getTotalPayment()));
        }

        // 총 상환액 테스트
        BigDecimal totalPayments = repaymentScheduleList.stream()
            .map(RepaymentSchedule::getTotalPayment)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expectedTotal = expectedMonthlyPayment.multiply(BigDecimal.valueOf(12));
        assertEquals(0, expectedTotal.compareTo(totalPayments));

        // 총 이자 테스트
        BigDecimal sumInterest = repaymentScheduleList.stream()
            .map(RepaymentSchedule::getInterestPayment)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(0, totalInterest.compareTo(sumInterest));
    }

    @DisplayName("원금상환 정상 산출 테스트 with 거치기간")
    @Test
    void calculateEqualPrincipalLoanRepayment() throws Exception {
        //given
        BigDecimal principal = BigDecimal.valueOf(1000000);
        BigDecimal term = BigDecimal.valueOf(12);
        BigDecimal gracePeriod = BigDecimal.valueOf(2);
        BigDecimal interestRate = BigDecimal.valueOf(0.12);
        RepaymentCalcServiceRequest repaymentCalcServiceRequest = RepaymentCalcServiceRequest.builder()
            .repaymentType(RepaymentType.EQUAL_PRINCIPAL)
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRate)
            .build();

        //when
        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculate(repaymentCalcServiceRequest);
        List<RepaymentSchedule> repaymentScheduleList = repaymentCalcResponse.getRepaymentSchedules();
        BigDecimal totalPrincipal = repaymentCalcResponse.getTotalPrincipal();
        BigDecimal totalInterest = repaymentCalcResponse.getTotalInterest();
        BigDecimal totalInstallments = repaymentCalcResponse.getTotalInstallments();

        //then
        assertEquals(12, repaymentScheduleList.size());
        assertEquals(BigDecimal.valueOf(12), totalInstallments);
        assertEquals(0, principal.compareTo(totalPrincipal));

        // 거치기간 테스트
        assertEquals(0, BigDecimal.ZERO.compareTo(repaymentScheduleList.get(0).getPrincipalPayment()));
        assertEquals(0, BigDecimal.ZERO.compareTo(repaymentScheduleList.get(1).getPrincipalPayment()));
        assertEquals(0, BigDecimal.valueOf(10000).compareTo(repaymentScheduleList.get(0).getInterestPayment()));
        assertEquals(0, BigDecimal.valueOf(10000).compareTo(repaymentScheduleList.get(1).getInterestPayment()));

        // 월상환 원금 테스트
        assertTrue(repaymentScheduleList.get(2).getPrincipalPayment().compareTo(BigDecimal.ZERO) > 0);
        BigDecimal expectedMonthlyPrincipalPayment = principal.divide(term.subtract(gracePeriod), 10, RoundingMode.HALF_UP);
        for (int i = gracePeriod.intValue(); i < term.intValue(); i++) {
            assertEquals(0, expectedMonthlyPrincipalPayment.compareTo(repaymentScheduleList.get(i).getPrincipalPayment()));
        }

        // 총 이자 테스트
        BigDecimal sumInterest = repaymentScheduleList.stream()
            .map(RepaymentSchedule::getInterestPayment)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expectedTotalInterest = BigDecimal.ZERO;
        BigDecimal monthlyInterestRate = interestRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal remainingPrincipal = principal;

        for (int i = 0; i < gracePeriod.intValue(); i++) {
            expectedTotalInterest = expectedTotalInterest.add(remainingPrincipal.multiply(monthlyInterestRate));
        }

        for (int i = gracePeriod.intValue(); i < term.intValue(); i++) {
            expectedTotalInterest = expectedTotalInterest.add(remainingPrincipal.multiply(monthlyInterestRate));
            remainingPrincipal = remainingPrincipal.subtract(expectedMonthlyPrincipalPayment);
        }
        assertEquals(0, expectedTotalInterest.compareTo(sumInterest));
        assertEquals(0, expectedTotalInterest.compareTo(totalInterest));

        // 최종회차 잔금 테스트
        assertEquals(0, BigDecimal.ZERO.compareTo(repaymentScheduleList.get(11).getRemainingPrincipal()));

        // 총 상환액 테스트
        BigDecimal totalPayments = repaymentScheduleList.stream()
            .map(RepaymentSchedule::getTotalPayment)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expectedTotal = expectedMonthlyPrincipalPayment.multiply(term.subtract(gracePeriod)).add(expectedTotalInterest);
        assertEquals(0, expectedTotal.compareTo(totalPayments));
    }

    @DisplayName("원금상환 정상 산출 테스트 without 거치기간")
    @Test
    void calculateEqualPrincipalLoanRepaymentWithoutGracePeriod() throws Exception {
        //given
        BigDecimal principal = BigDecimal.valueOf(1200000);
        BigDecimal term = BigDecimal.valueOf(12);
        BigDecimal gracePeriod = BigDecimal.ZERO;
        BigDecimal interestRate = BigDecimal.valueOf(0.12);
        RepaymentCalcServiceRequest repaymentCalcServiceRequest = RepaymentCalcServiceRequest.builder()
            .repaymentType(RepaymentType.EQUAL_PRINCIPAL)
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRate)
            .build();

        //when
        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculate(repaymentCalcServiceRequest);
        List<RepaymentSchedule> repaymentScheduleList = repaymentCalcResponse.getRepaymentSchedules();
        BigDecimal totalPrincipal = repaymentCalcResponse.getTotalPrincipal();
        BigDecimal totalInterest = repaymentCalcResponse.getTotalInterest();
        BigDecimal totalInstallments = repaymentCalcResponse.getTotalInstallments();

        //then
        assertEquals(12, repaymentScheduleList.size());
        assertEquals(BigDecimal.valueOf(12), totalInstallments);
        assertEquals(0, principal.compareTo(totalPrincipal));

        // 거치기간 테스트
        assertEquals(0, BigDecimal.valueOf(100000).compareTo(repaymentScheduleList.get(0).getPrincipalPayment()));
        assertEquals(0, BigDecimal.valueOf(100000).compareTo(repaymentScheduleList.get(1).getPrincipalPayment()));
        assertEquals(0, BigDecimal.valueOf(12000).compareTo(repaymentScheduleList.get(0).getInterestPayment()));
        assertEquals(0, BigDecimal.valueOf(11000).compareTo(repaymentScheduleList.get(1).getInterestPayment()));

        // 월상환 원금 테스트
        assertTrue(repaymentScheduleList.get(2).getPrincipalPayment().compareTo(BigDecimal.ZERO) > 0);
        BigDecimal expectedMonthlyPrincipalPayment = principal.divide(term.subtract(gracePeriod), 10, RoundingMode.HALF_UP);
        for (int i = gracePeriod.intValue(); i < term.intValue(); i++) {
            assertEquals(0, expectedMonthlyPrincipalPayment.compareTo(repaymentScheduleList.get(i).getPrincipalPayment()));
        }

        // 총 이자 테스트
        BigDecimal sumInterest = repaymentScheduleList.stream()
            .map(RepaymentSchedule::getInterestPayment)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expectedTotalInterest = BigDecimal.ZERO;
        BigDecimal monthlyInterestRate = interestRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal remainingPrincipal = principal;

        for (int i = 0; i < gracePeriod.intValue(); i++) {
            expectedTotalInterest = expectedTotalInterest.add(remainingPrincipal.multiply(monthlyInterestRate));
        }

        for (int i = gracePeriod.intValue(); i < term.intValue(); i++) {
            expectedTotalInterest = expectedTotalInterest.add(remainingPrincipal.multiply(monthlyInterestRate));
            remainingPrincipal = remainingPrincipal.subtract(expectedMonthlyPrincipalPayment);
        }
        assertEquals(0, expectedTotalInterest.compareTo(sumInterest));
        assertEquals(0, expectedTotalInterest.compareTo(totalInterest));

        // 최종회차 잔금 테스트
        assertEquals(0, BigDecimal.ZERO.compareTo(repaymentScheduleList.get(11).getRemainingPrincipal()));

        // 총 상환액 테스트
        BigDecimal totalPayments = repaymentScheduleList.stream()
            .map(RepaymentSchedule::getTotalPayment)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expectedTotal = expectedMonthlyPrincipalPayment.multiply(term.subtract(gracePeriod)).add(expectedTotalInterest);
        assertEquals(0, expectedTotal.compareTo(totalPayments));
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal monthlyInterestRate, BigDecimal numberOfPayments) {
        BigDecimal one = BigDecimal.ONE;
        BigDecimal onePlusRate = one.add(monthlyInterestRate);
        BigDecimal powValue = onePlusRate.pow(numberOfPayments.intValue());

        BigDecimal numerator = principal.multiply(monthlyInterestRate).multiply(powValue);
        BigDecimal denominator = powValue.subtract(one);

        return numerator.divide(denominator, 0, RoundingMode.HALF_UP);
    }


}