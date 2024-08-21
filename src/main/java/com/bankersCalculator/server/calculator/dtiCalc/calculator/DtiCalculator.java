package com.bankersCalculator.server.calculator.dtiCalc.calculator;

import com.bankersCalculator.server.calculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcServiceRequest;
import com.bankersCalculator.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.bankersCalculator.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.bankersCalculator.server.common.enums.LoanType;
import com.bankersCalculator.server.common.enums.RepaymentType;
import org.springframework.stereotype.Component;

@Component
public class DtiCalculator {
    private static final int MAX_TERM_FOR_BULLET = 120;

    private final RepaymentCalcService repaymentCalcService;

    public DtiCalculator(RepaymentCalcService repaymentCalcService) {
        this.repaymentCalcService = repaymentCalcService;
    }

    public DtiCalcResult calculateDti(DtiCalcServiceRequest.LoanStatus loanStatus) {
        if (loanStatus.getLoanType() == LoanType.MORTGAGE) {
            return calculateMortgageLoanDti(loanStatus);
        } else {
            return calculateOtherLoanDti(loanStatus);
        }
    }

    private DtiCalcResult calculateMortgageLoanDti(DtiCalcServiceRequest.LoanStatus loanStatus) {
        RepaymentType repaymentType = loanStatus.getRepaymentType();

        if (repaymentType == RepaymentType.BULLET) {
            int term = Math.min(MAX_TERM_FOR_BULLET, loanStatus.getTerm());
            return dtiCalcForBulletLoan(loanStatus, term);
        } else if (repaymentType == RepaymentType.AMORTIZING || repaymentType == RepaymentType.EQUAL_PRINCIPAL) {
            return dtiCalcForInstallmentRepaymentMortgageLoan(loanStatus);
        } else {
            throw new IllegalArgumentException("해당 대출유형은 지원하지 않습니다. " + repaymentType);
        }
    }

    private DtiCalcResult calculateOtherLoanDti(DtiCalcServiceRequest.LoanStatus loanStatus) {
        int term = loanStatus.getTerm();
        double principal = loanStatus.getPrincipal();
        double interestRatePercentage = loanStatus.getInterestRate();
        double annualInterestRepayment = principal * interestRatePercentage;

        return DtiCalcResult.builder()
                .principal(principal)
                .term(term)
                .annualInterestRepayment(annualInterestRepayment)
                .build();
    }

    private DtiCalcResult dtiCalcForInstallmentRepaymentMortgageLoan(DtiCalcServiceRequest.LoanStatus loanStatus) {
        double principal = loanStatus.getPrincipal();
        int term = loanStatus.getTerm();

        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(loanStatus.toRepaymentCalcServiceRequest());
        double totalInterest = repaymentCalcResponse.getTotalInterest();
        double annualPrincipalRepayment = principal / term * 12;
        double annualInterestRepayment = totalInterest / term * 12;

        return DtiCalcResult.builder()
                .principal(principal)
                .term(term)
                .annualPrincipalRepayment(annualPrincipalRepayment)
                .annualInterestRepayment(annualInterestRepayment)
                .build();
    }


    private DtiCalcResult dtiCalcForBulletLoan(DtiCalcServiceRequest.LoanStatus loanStatus, int maxTerm) {
        double principal = loanStatus.getPrincipal();
        int term = loanStatus.getTerm();
        double interestRate = loanStatus.getInterestRate();

        double annualPrincipalRepayment = principal / maxTerm * 12;
        double annalInterestRepayment = principal * interestRate;

        return DtiCalcResult.builder()
                .principal(principal)
                .term(term)
                .annualPrincipalRepayment(annualPrincipalRepayment)
                .annualInterestRepayment(annalInterestRepayment)
                .build();
    }
}
