package com.myZipPlan.server.calculator.repaymentCalc.service;

import com.myZipPlan.server.calculator.repaymentCalc.domain.RepaymentSchedule;
import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcServiceRequest;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class RepaymentCalcService {

    // TODO: 언젠가(사용자가 생기면..) BigDecimal로 자료형 바꿀 것.

    public RepaymentCalcResponse calculateRepayment(RepaymentCalcServiceRequest repaymentCalcServiceRequest) {
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

        double principalAmount = repaymentCalcServiceRequest.getPrincipal();
        int repaymentTermInMonths = repaymentCalcServiceRequest.getTerm();
        double annualInterestRate = repaymentCalcServiceRequest.getInterestRate();

        double annualInterest = principalAmount * annualInterestRate;
        double monthlyInterest = annualInterest / 12;
        double totalInterest = monthlyInterest * repaymentTermInMonths;

        List<RepaymentSchedule> repaymentScheduleList = new ArrayList<>();
        double remainingPrincipal = principalAmount;

        for (int i = 1; i <= repaymentTermInMonths; i++) {

            boolean lastInstallment = i == repaymentTermInMonths;
            double principalPayment = lastInstallment ? principalAmount : 0;
            remainingPrincipal -= principalPayment;

            RepaymentSchedule repaymentSchedule = RepaymentSchedule.builder()
                .installmentNumber(i)
                .principalPayment(principalPayment)
                .interestPayment(monthlyInterest)
                .totalPayment(principalPayment + monthlyInterest)
                .remainingPrincipal(remainingPrincipal)
                .build();
            repaymentScheduleList.add(repaymentSchedule);
        }

        return RepaymentCalcResponse.builder()
            .repaymentSchedules(repaymentScheduleList)
            .totalPrincipal(principalAmount)
            .totalInterest(totalInterest)
            .totalInstallments(repaymentScheduleList.size())
            .build();
    }

    private RepaymentCalcResponse calculateAmortizingLoanRepayment(RepaymentCalcServiceRequest repaymentCalcServiceRequest) {
        double principalAmount = repaymentCalcServiceRequest.getPrincipal();
        int repaymentTermInMonths = repaymentCalcServiceRequest.getTerm();
        int gracePeriod = repaymentCalcServiceRequest.getGracePeriod();
        double monthlyInterestRate = repaymentCalcServiceRequest.getInterestRate() / 12;

        List<RepaymentSchedule> repaymentScheduleList = new ArrayList<>();
        int numberOfPayments = repaymentTermInMonths - gracePeriod;
        double monthlyPayment = calculateAmortizingLoanMonthlyPayment(principalAmount, monthlyInterestRate, numberOfPayments);

        double remainingPrincipal = principalAmount;
        double totalInterest = 0;

        for (int i = 1; i <= repaymentTermInMonths; i++) {
            double interestPayment = remainingPrincipal * monthlyInterestRate;
            double principalPayment;
            double totalPayment;

            if (i <= gracePeriod) {
                principalPayment = 0;
                totalPayment = interestPayment;
            } else {
                principalPayment = monthlyPayment - interestPayment;
                totalPayment = monthlyPayment;
            }

            remainingPrincipal -= principalPayment;
            totalInterest += interestPayment;

            RepaymentSchedule repaymentSchedule = RepaymentSchedule.builder()
                .installmentNumber(i)
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
            .totalInstallments(repaymentScheduleList.size())
            .build();
    }

    private RepaymentCalcResponse calculateEqualPrincipalLoanRepayment(RepaymentCalcServiceRequest repaymentCalcServiceRequest) {
        double principalAmount = repaymentCalcServiceRequest.getPrincipal();
        int repaymentTermInMonths = repaymentCalcServiceRequest.getTerm();
        int gracePeriod = repaymentCalcServiceRequest.getGracePeriod();
        double monthlyInterestRate = repaymentCalcServiceRequest.getInterestRate() / 12;

        List<RepaymentSchedule> repaymentScheduleList = new ArrayList<>();
        int numberOfPayments = repaymentTermInMonths - gracePeriod;
        double monthlyPayment = principalAmount / numberOfPayments;

        double remainingPrincipal = principalAmount;
        double totalInterest = 0;

        for (int i = 1; i <= repaymentTermInMonths; i++) {
            double interestPayment = remainingPrincipal * monthlyInterestRate;
            double principalPayment;
            double totalPayment;

            if (i <= gracePeriod) {
                principalPayment = 0;
                totalPayment = interestPayment;
            } else {
                principalPayment = monthlyPayment;
                totalPayment = monthlyPayment + interestPayment;
            }

            remainingPrincipal -= principalPayment;
            totalInterest += interestPayment;

            RepaymentSchedule repaymentSchedule = RepaymentSchedule.builder()
                .installmentNumber(i)
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
            .totalInstallments(repaymentScheduleList.size())
            .build();
    }

    private double calculateAmortizingLoanMonthlyPayment(double principal, double monthlyInterestRate, int numberOfPayments) {
        return principal * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfPayments))
            / (Math.pow(1 + monthlyInterestRate, numberOfPayments) - 1);
    }
}
