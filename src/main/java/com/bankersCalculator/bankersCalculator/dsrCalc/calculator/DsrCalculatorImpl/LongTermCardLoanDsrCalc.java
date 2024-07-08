package com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculator;
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
}
