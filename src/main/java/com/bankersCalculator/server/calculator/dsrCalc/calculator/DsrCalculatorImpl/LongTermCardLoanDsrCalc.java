package com.bankersCalculator.server.calculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.bankersCalculator.server.calculator.dsrCalc.calculator.DsrCalculator;
import com.bankersCalculator.server.calculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.server.calculator.dsrCalc.dto.DsrCalcServiceRequest;
import com.bankersCalculator.server.common.enums.LoanType;
import com.bankersCalculator.server.common.enums.RepaymentType;
import org.springframework.stereotype.Component;

@Component
public class LongTermCardLoanDsrCalc implements DsrCalculator {

    private static final int MAX_TERM_FOR_BULLET = 36;
    private static final int MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = 60;

    @Override
    public LoanType getLoanType() {
        return LoanType.LONG_TERM_CARD_LOAN;
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
            int term = Math.min(loanStatus.getTerm(), getMaxTermForBullet());
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForBulletLoan(loanStatus, term);
        }
        if (repaymentType == RepaymentType.AMORTIZING) {
            int term = Math.min(loanStatus.getTerm(), getMaxTermForEqualPrincipalAndAmortizing());
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForAmortizingLoan(loanStatus, term);
        }
        if (repaymentType == RepaymentType.EQUAL_PRINCIPAL) {
            int term = Math.min(loanStatus.getTerm(), getMaxTermForEqualPrincipalAndAmortizing());
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForEqualPrincipalLoan(loanStatus, term);
        }
        return dsrCalcResult;
    }
}
