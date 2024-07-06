package com.bankersCalculator.bankersCalculator.repaymentCalc.service;

import com.bankersCalculator.bankersCalculator.repaymentCalc.domain.RepaymentSchedule;
import com.bankersCalculator.bankersCalculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.bankersCalculator.bankersCalculator.repaymentCalc.dto.RepaymentCalcServiceRequest;
import com.bankersCalculator.bankersCalculator.repaymentCalc.dto.RepaymentType;
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
        RepaymentType type = repaymentCalcServiceRequest.getType();

        RepaymentCalcResponse response = RepaymentCalcResponse.builder().build();

        if (type == RepaymentType.Bullet) {
             response = calculateBulletLoanRepayment(repaymentCalcServiceRequest);
        }
        if (type == RepaymentType.Amortizing) {
            response = calculateAmortizingLoanRepayment(repaymentCalcServiceRequest);
        }

        return response;
    }

    private RepaymentCalcResponse calculateBulletLoanRepayment(RepaymentCalcServiceRequest repaymentCalcServiceRequest) {

        double principalAmount = repaymentCalcServiceRequest.getPrincipal();
        int repaymentTermInMonths = repaymentCalcServiceRequest.getTerm();
        double annualInterestRate = repaymentCalcServiceRequest.getInterestRateAsDecimal();

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
                .remainingPrinciple(remainingPrincipal)
                .build();
            repaymentScheduleList.add(repaymentSchedule);
        }

        return RepaymentCalcResponse.builder()
            .repaymentScheduleList(repaymentScheduleList)
            .totalPrincipal(principalAmount)
            .totalInterest(totalInterest)
            .totalInstallments(repaymentScheduleList.size())
            .build();
    }

    private RepaymentCalcResponse calculateAmortizingLoanRepayment(RepaymentCalcServiceRequest repaymentCalcServiceRequest) {
        double principalAmount = repaymentCalcServiceRequest.getPrincipal();
        int repaymentTermInMonths = repaymentCalcServiceRequest.getTerm();
        int gracePeriod = repaymentCalcServiceRequest.getGracePeriod();
        double monthlyInterestRate = repaymentCalcServiceRequest.getInterestRateAsDecimal()  / 12;


        List<RepaymentSchedule> repaymentScheduleList = new ArrayList<>();
        int numberOfPayments = repaymentTermInMonths - gracePeriod;
        double monthlyPayment = calculateMonthlyPayment(principalAmount, monthlyInterestRate, numberOfPayments);

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
                .remainingPrinciple(remainingPrincipal)
                .build();

            repaymentScheduleList.add(repaymentSchedule);
        }

        return RepaymentCalcResponse.builder()
            .repaymentScheduleList(repaymentScheduleList)
            .totalPrincipal(principalAmount)
            .totalInterest(totalInterest)
            .totalInstallments(repaymentScheduleList.size())
            .build();

    }

    private double calculateMonthlyPayment(double principal, double monthlyInterestRate, int numberOfPayments) {
        return principal * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfPayments))
            / (Math.pow(1 + monthlyInterestRate, numberOfPayments) - 1);
    }
}
