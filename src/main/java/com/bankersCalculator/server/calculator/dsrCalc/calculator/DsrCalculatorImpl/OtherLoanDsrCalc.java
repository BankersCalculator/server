package com.bankersCalculator.server.calculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.bankersCalculator.server.calculator.dsrCalc.calculator.DsrCalculator;
import com.bankersCalculator.server.calculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.server.calculator.dsrCalc.dto.DsrCalcServiceRequest;
import com.bankersCalculator.server.common.enums.calculator.LoanType;
import com.bankersCalculator.server.common.enums.calculator.RepaymentType;
import org.springframework.stereotype.Component;

@Component
public class OtherLoanDsrCalc implements DsrCalculator {

    private static final int MAX_TERM_FOR_BULLET = -1;
    private static final int MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = -1;

    @Override
    public LoanType getLoanType() {
        return LoanType.OTHER_LOAN;
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
        int term = loanStatus.getTerm();

        if (repaymentType == RepaymentType.BULLET) {
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForBulletLoan(loanStatus, term);
        }
        if (repaymentType == RepaymentType.AMORTIZING) {
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForAmortizingLoan(loanStatus, term);
        }
        if (repaymentType == RepaymentType.EQUAL_PRINCIPAL) {
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForEqualPrincipalLoan(loanStatus, term);
        }
        return dsrCalcResult;
    }
}
