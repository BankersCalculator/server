package com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.common.enums.RepaymentType;
import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculator;
import com.bankersCalculator.bankersCalculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class PersonalLoanDsrCalc implements DsrCalculator {

    private static final int MAX_TERM_FOR_BULLET = 60;
    private static final int MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = 120;

    @Override
    public LoanType getLoanType() {
        return LoanType.PERSONAL_LOAN;
    }

    @Override
    public int getMaxTermForBullet() {
        return MAX_TERM_FOR_BULLET;
    }

    @Override
    public int getMaxTermForEqualPrincipalAndAmortizing() {
        return MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING;
    }


    @Override
    public DsrCalcResult calculateDsr(DsrCalcServiceRequest.LoanStatus loanStatus) {
        RepaymentType repaymentType = loanStatus.getRepaymentType();
        DsrCalcResult dsrCalcResult = DsrCalcResult.builder().build();

        if (repaymentType == RepaymentType.BULLET) {
            int maxTermForBullet = getMaxTermForBullet();

            dsrCalcResult = dsrCommonCaclulator.dsrCalcForBulletLoan(loanStatus, maxTermForBullet);
        }
        if (repaymentType == RepaymentType.AMORTIZING) {
            int term = calculateAdjustedTerm(loanStatus);

            dsrCalcResult = dsrCommonCaclulator.dsrCalcForAmortizingLoan(loanStatus, term);
        }
        if (repaymentType == RepaymentType.EQUAL_PRINCIPAL) {
            int term = calculateAdjustedTerm(loanStatus);

            dsrCalcResult = dsrCommonCaclulator.dsrCalcForEqualPrincipalLoan(loanStatus, term);
        }
        return dsrCalcResult;
    }

    private int calculateAdjustedTerm(DsrCalcServiceRequest.LoanStatus loanStatus) {
        int maxTerm = getMaxTermForEqualPrincipalAndAmortizing();
        int actualTerm = loanStatus.getTerm();
        return Math.max(Math.min(maxTerm, actualTerm), 60);

    }
}
