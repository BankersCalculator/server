package com.myZipPlan.server.calculator.dsrCalc.calculator;

import com.myZipPlan.server.calculator.dsrCalc.domain.DsrCalcResult;
import com.myZipPlan.server.calculator.dsrCalc.dto.DsrCalcServiceRequest;
import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.myZipPlan.server.calculator.repaymentCalc.service.RepaymentCalcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DsrCommonCalculator {

    private final RepaymentCalcService repaymentCalcService;

    public DsrCalcResult dsrCalcForBulletLoan(DsrCalcServiceRequest.LoanStatus loanStatus, int maxTerm) {
        double principal = loanStatus.getPrincipal();
        int term = loanStatus.getTerm();
        double interestRate = loanStatus.getInterestRate();

        double annualPrincipalRepayment = principal / maxTerm * 12;
        double annalInterestRepayment = principal * interestRate;

        return DsrCalcResult.builder()
            .principal(principal)
            .term(term)
            .annualPrincipalRepayment(annualPrincipalRepayment)
            .annualInterestRepayment(annalInterestRepayment)
            .build();
    }

    public DsrCalcResult dsrCalcForAmortizingLoan(DsrCalcServiceRequest.LoanStatus loanStatus, int maxTerm) {
        double principal = loanStatus.getPrincipal();
        int term = loanStatus.getTerm();

        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(loanStatus.toRepaymentCalcServiceRequest());
        double totalInterest = repaymentCalcResponse.getTotalInterest();

        double annualPrincipalRepayment = principal / maxTerm * 12;
        double annalInterestRepayment = totalInterest / term * 12;

        return DsrCalcResult.builder()
            .principal(principal)
            .term(term)
            .annualPrincipalRepayment(annualPrincipalRepayment)
            .annualInterestRepayment(annalInterestRepayment)
            .build();
    }

    public DsrCalcResult dsrCalcForEqualPrincipalLoan(DsrCalcServiceRequest.LoanStatus loanStatus, int maxTerm) {
        double principal = loanStatus.getPrincipal();
        int term = loanStatus.getTerm();

        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(loanStatus.toRepaymentCalcServiceRequest());
        double totalInterest = repaymentCalcResponse.getTotalInterest();

        double annualPrincipalRepayment = principal / maxTerm * 12;
        double annalInterestRepayment = totalInterest / term * 12;

        return DsrCalcResult.builder()
            .principal(principal)
            .term(term)
            .annualPrincipalRepayment(annualPrincipalRepayment)
            .annualInterestRepayment(annalInterestRepayment)
            .build();
    }


    public DsrCalcResult dsrCalcWithoutPrincipalRepayment(DsrCalcServiceRequest.LoanStatus loanStatus) {
        double principal = loanStatus.getPrincipal();
        int term = loanStatus.getTerm();
        double interestRate = loanStatus.getInterestRate();

        double annualPrincipalRepayment = 0;
        double annalInterestRepayment = principal * interestRate;

        return DsrCalcResult.builder()
            .principal(principal)
            .term(term)
            .annualPrincipalRepayment(annualPrincipalRepayment)
            .annualInterestRepayment(annalInterestRepayment)
            .build();
    }
}
