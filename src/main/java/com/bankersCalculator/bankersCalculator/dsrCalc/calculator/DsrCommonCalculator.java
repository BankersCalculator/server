package com.bankersCalculator.bankersCalculator.dsrCalc.calculator;

import com.bankersCalculator.bankersCalculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class DsrCommonCalculator {

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
}
