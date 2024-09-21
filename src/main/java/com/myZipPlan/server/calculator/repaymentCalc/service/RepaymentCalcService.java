package com.myZipPlan.server.calculator.repaymentCalc.service;

import com.myZipPlan.server.calculator.repaymentCalc.domain.RepaymentSchedule;
import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcServiceRequest;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class RepaymentCalcService {

    public RepaymentCalcResponse calculate(RepaymentCalcServiceRequest repaymentCalcServiceRequest) {
        RepaymentType repaymentType = repaymentCalcServiceRequest.getRepaymentType();

        RepaymentCalcResponse response = RepaymentCalcResponse.builder().build();

        if (repaymentType == RepaymentType.BULLET) {
            response = calculateBulletLoanRepayment(repaymentCalcServiceRequest);
        }
        if (repaymentType == RepaymentType.AMORTIZING) {
            response = calculateAmortizingLoanRepayment(repaymentCalcServiceRequest);
        }
        if (repaymentType == RepaymentType.EQUAL_PRINCIPAL) {
            response = calculateEqualPrincipalLoanRepayment(repaymentCalcServiceRequest);
        }

        return response;
    }

    private RepaymentCalcResponse calculateBulletLoanRepayment(RepaymentCalcServiceRequest repaymentCalcServiceRequest) {

        BigDecimal principalAmount = repaymentCalcServiceRequest.getPrincipal();
        BigDecimal repaymentTermInMonths = repaymentCalcServiceRequest.getTerm();
        BigDecimal annualInterestRate = repaymentCalcServiceRequest.getInterestRate();

        BigDecimal annualInterest = principalAmount.multiply(annualInterestRate);
        BigDecimal monthlyInterest = annualInterest.divide(BigDecimal.valueOf(12), 4, RoundingMode.DOWN);
        BigDecimal totalInterest = monthlyInterest.multiply(repaymentTermInMonths);

        List<RepaymentSchedule> repaymentScheduleList = new ArrayList<>();
        BigDecimal remainingPrincipal = principalAmount;

        int repaymentTermInMonthsInt = repaymentTermInMonths.intValue();

        for (int i = 1; i <= repaymentTermInMonthsInt; i++) {

            boolean lastInstallment = i == repaymentTermInMonthsInt;
            BigDecimal principalPayment = lastInstallment ? principalAmount : BigDecimal.ZERO;
            remainingPrincipal = remainingPrincipal.subtract(principalPayment);

            RepaymentSchedule repaymentSchedule = RepaymentSchedule.builder()
                .installmentNumber(BigDecimal.valueOf(i))
                .principalPayment(principalPayment)
                .interestPayment(monthlyInterest)
                .totalPayment(principalPayment.add(monthlyInterest))
                .remainingPrincipal(remainingPrincipal)
                .build();
            repaymentScheduleList.add(repaymentSchedule);
        }

        return RepaymentCalcResponse.builder()
            .repaymentSchedules(repaymentScheduleList)
            .totalPrincipal(principalAmount)
            .totalInterest(totalInterest)
            .totalInstallments(BigDecimal.valueOf(repaymentScheduleList.size()))
            .build();
    }

    private RepaymentCalcResponse calculateAmortizingLoanRepayment(RepaymentCalcServiceRequest repaymentCalcServiceRequest) {
        BigDecimal principalAmount = repaymentCalcServiceRequest.getPrincipal();
        BigDecimal repaymentTermInMonths = repaymentCalcServiceRequest.getTerm();
        BigDecimal gracePeriod = repaymentCalcServiceRequest.getGracePeriod();
        BigDecimal monthlyInterestRate = repaymentCalcServiceRequest.getInterestRate().divide(BigDecimal.valueOf(12), 4, RoundingMode.DOWN);

        List<RepaymentSchedule> repaymentScheduleList = new ArrayList<>();
        BigDecimal numberOfPayments = repaymentTermInMonths.subtract(gracePeriod);
        BigDecimal monthlyPayment = calculateAmortizingLoanMonthlyPayment(principalAmount, monthlyInterestRate, numberOfPayments);

        BigDecimal remainingPrincipal = principalAmount;
        BigDecimal totalInterest = BigDecimal.ZERO;

        int repaymentTermInMonthsInt = repaymentTermInMonths.intValue();

        for (int i = 1; i <= repaymentTermInMonthsInt; i++) {

            BigDecimal interestPayment = remainingPrincipal.multiply(monthlyInterestRate);
            BigDecimal principalPayment;
            BigDecimal totalPayment;

            BigDecimal currentMonth = BigDecimal.valueOf(i);
            if (currentMonth.compareTo(gracePeriod) <= 0) {
                principalPayment = BigDecimal.ZERO;
                totalPayment = interestPayment;
            } else {
                principalPayment = monthlyPayment.subtract(interestPayment);
                totalPayment = monthlyPayment;
            }

            remainingPrincipal = remainingPrincipal.subtract(principalPayment);
            totalInterest = totalInterest.add(interestPayment);

            RepaymentSchedule repaymentSchedule = RepaymentSchedule.builder()
                .installmentNumber(BigDecimal.valueOf(i))
                .principalPayment(principalPayment)
                .interestPayment(interestPayment)
                .totalPayment(totalPayment)
                .remainingPrincipal(remainingPrincipal)
                .build();

            repaymentScheduleList.add(repaymentSchedule);
        }

        return RepaymentCalcResponse.builder()
            .repaymentSchedules(repaymentScheduleList)
            .totalPrincipal(principalAmount)
            .totalInterest(totalInterest)
            .totalInstallments(BigDecimal.valueOf(repaymentScheduleList.size()))
            .build();
    }

    private RepaymentCalcResponse calculateEqualPrincipalLoanRepayment(RepaymentCalcServiceRequest repaymentCalcServiceRequest) {
        BigDecimal principalAmount = repaymentCalcServiceRequest.getPrincipal();
        BigDecimal repaymentTermInMonths = repaymentCalcServiceRequest.getTerm();
        BigDecimal gracePeriod = repaymentCalcServiceRequest.getGracePeriod();
        BigDecimal monthlyInterestRate = repaymentCalcServiceRequest.getInterestRate().divide(BigDecimal.valueOf(12), 4, RoundingMode.DOWN);

        List<RepaymentSchedule> repaymentScheduleList = new ArrayList<>();
        BigDecimal numberOfPayments = repaymentTermInMonths.subtract(gracePeriod);
        BigDecimal monthlyPayment = principalAmount.divide(numberOfPayments, 4, RoundingMode.DOWN);

        BigDecimal remainingPrincipal = principalAmount;
        BigDecimal totalInterest = BigDecimal.ZERO;

        int repaymentTermInMonthsInt = repaymentTermInMonths.intValue();

        for (int i = 1; i <= repaymentTermInMonthsInt; i++) {
            BigDecimal interestPayment = remainingPrincipal.multiply(monthlyInterestRate);
            BigDecimal principalPayment;
            BigDecimal totalPayment;

            BigDecimal currentMonth = BigDecimal.valueOf(i);
            if (currentMonth.compareTo(gracePeriod) <= 0) {
                principalPayment = BigDecimal.ZERO;
                totalPayment = interestPayment;
            } else {
                principalPayment = monthlyPayment;
                totalPayment = monthlyPayment.add( interestPayment);
            }

            remainingPrincipal = remainingPrincipal .subtract(principalPayment);
            totalInterest = totalInterest.add( interestPayment);

            RepaymentSchedule repaymentSchedule = RepaymentSchedule.builder()
                .installmentNumber(BigDecimal.valueOf(i))
                .principalPayment(principalPayment)
                .interestPayment(interestPayment)
                .totalPayment(totalPayment)
                .remainingPrincipal(remainingPrincipal)
                .build();

            repaymentScheduleList.add(repaymentSchedule);
        }
        return RepaymentCalcResponse.builder()
            .repaymentSchedules(repaymentScheduleList)
            .totalPrincipal(principalAmount)
            .totalInterest(totalInterest)
            .totalInstallments(BigDecimal.valueOf(repaymentScheduleList.size()))
            .build();
    }

    private BigDecimal calculateAmortizingLoanMonthlyPayment(BigDecimal principal, BigDecimal monthlyInterestRate, BigDecimal numberOfPayments) {
        BigDecimal one = BigDecimal.ONE;
        BigDecimal onePlusRate = one.add(monthlyInterestRate);
        BigDecimal denominator = onePlusRate.pow(numberOfPayments.intValue()).subtract(one);
        BigDecimal numerator = principal.multiply(monthlyInterestRate).multiply(onePlusRate.pow(numberOfPayments.intValue()));

        return numerator.divide(denominator, 10, RoundingMode.HALF_UP);
    }
}
