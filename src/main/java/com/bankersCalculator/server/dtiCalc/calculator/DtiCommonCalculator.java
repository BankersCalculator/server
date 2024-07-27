package com.bankersCalculator.server.dtiCalc.calculator;

import com.bankersCalculator.server.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.server.dtiCalc.dto.DtiCalcServiceRequest;
import com.bankersCalculator.server.repaymentCalc.dto.RepaymentCalcResponse;
import com.bankersCalculator.server.repaymentCalc.service.RepaymentCalcService;
import lombok.RequiredArgsConstructor;

import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DtiCommonCalculator {
    private final RepaymentCalcService repaymentCalcService;
    DecimalFormat decimalFormat = new DecimalFormat("#,###");


    //만기일시상환
    public DtiCalcResult dtiCalcForBulletLoan(DtiCalcServiceRequest.LoanStatus loanStatus, int maxTerm) {
        double principal = loanStatus.getPrincipal();
        int term = loanStatus.getTerm();
        double interestRate = loanStatus.getInterestRate();

        double annualPrincipalRepayment = principal / maxTerm * 12;
        double annalInterestRepayment = principal * interestRate;
        
        System.out.println("DtiCommonCalculator, principal : " + annalInterestRepayment + " term : " + term + " interestRate : " + interestRate);  
        System.out.println("annualPrincipalRepayment : " + decimalFormat.format(annualPrincipalRepayment));
        System.out.println("annalInterestRepayment : " + annalInterestRepayment);

        return DtiCalcResult.builder()
            .principal(principal)
            .term(term)
            .annualPrincipalRepayment(annualPrincipalRepayment)
            .annualInterestRepayment(annalInterestRepayment)
            .build();
    }

    //원리금균등분할상환
    public DtiCalcResult dtiCalcForAmortizingLoan(DtiCalcServiceRequest.LoanStatus loanStatus, int maxTerm) {
        double principal = loanStatus.getPrincipal();
        int term = loanStatus.getTerm();

        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(loanStatus.toRepaymentCalcServiceRequest());
        double totalInterest = repaymentCalcResponse.getTotalInterest();

        double annualPrincipalRepayment = principal / maxTerm * 12;
        double annalInterestRepayment = totalInterest / term * 12;

        return DtiCalcResult.builder()
            .principal(principal)
            .term(term)
            .annualPrincipalRepayment(annualPrincipalRepayment)
            .annualInterestRepayment(annalInterestRepayment)
            .build();
    }

    //원금균등분할상환
    public DtiCalcResult dtiCalcForEqualPrincipalLoan(DtiCalcServiceRequest.LoanStatus loanStatus, int maxTerm) {
        double principal = loanStatus.getPrincipal();
        int term = loanStatus.getTerm();

        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(loanStatus.toRepaymentCalcServiceRequest());
        double totalInterest = repaymentCalcResponse.getTotalInterest();

        double annualPrincipalRepayment = principal / maxTerm * 12;
        double annalInterestRepayment = totalInterest / term * 12;

        return DtiCalcResult.builder()
            .principal(principal)
            .term(term)
            .annualPrincipalRepayment(annualPrincipalRepayment)
            .annualInterestRepayment(annalInterestRepayment)
            .build();
    }
}
