package com.bankersCalculator.server.calculator.dtiCalc.calculator;
import com.bankersCalculator.server.calculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcServiceRequest;
import com.bankersCalculator.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.bankersCalculator.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.bankersCalculator.server.common.enums.LoanType;
import com.bankersCalculator.server.common.enums.RepaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DtiCalculator {
    private static final int MAX_TERM_FOR_BULLET = 120;

    private final DtiCommonCalculator dtiCommonCalculator;
    private final RepaymentCalcService repaymentCalcService;

    public DtiCalculator(DtiCommonCalculator dtiCommonCalculator, RepaymentCalcService repaymentCalcService) {
        this.dtiCommonCalculator = dtiCommonCalculator;
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
            return dtiCommonCalculator.dtiCalcForBulletLoan(loanStatus, term);
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
        double annualInterestRepayment = totalInterest / term * 12;

        return DtiCalcResult.builder()
                .principal(principal)
                .term(term)
                .annualInterestRepayment(annualInterestRepayment)
                .build();
    }
}
