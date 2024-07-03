package com.bankersCalculator.bankersCalculator.repaymentCalc.service;

import com.bankersCalculator.bankersCalculator.repaymentCalc.dto.RepaymentCalcDto;
import com.bankersCalculator.bankersCalculator.repaymentCalc.dto.RepaymentSchedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class RepaymentCalcService {

    public List<RepaymentSchedule> calculateBulletLoanRepament(RepaymentCalcDto repaymentCalcDto) {

        double principalAmount = repaymentCalcDto.getPrincipal();
        int repaymentTermInMonths = repaymentCalcDto.getTerm();
        double annualInterestRate = repaymentCalcDto.getInterestRate();

        double annualInterest = principalAmount * annualInterestRate;
        double monthlyInterest = annualInterest / 12;
        double totalInterest = monthlyInterest * repaymentTermInMonths;

        List<RepaymentSchedule> repaymentScheduleList = new ArrayList<>();

        for (int i = 1; i <= repaymentTermInMonths; i++) {

            boolean lastInstallment = i == repaymentTermInMonths;
            double principalPayment = lastInstallment ? principalAmount : 0;
            principalAmount -= principalPayment;

            RepaymentSchedule repaymentSchedule = RepaymentSchedule.builder()
                .installmentNumber(i)
                .principalPayment(principalPayment)
                .interestPayment(monthlyInterest)
                .totalPayment(principalPayment + monthlyInterest)
                .remainingPrinciple(principalAmount)
                .build();
            repaymentScheduleList.add(repaymentSchedule);
        }

        return repaymentScheduleList;
    }

}
