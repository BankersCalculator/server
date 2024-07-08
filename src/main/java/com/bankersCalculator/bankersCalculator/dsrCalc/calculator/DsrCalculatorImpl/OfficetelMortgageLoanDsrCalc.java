package com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculator;
import org.springframework.stereotype.Component;

@Component
public class OfficetelMortgageLoanDsrCalc implements DsrCalculator {

    private static final int MAX_TERM_FOR_BULLET = 96;
    private static final int MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = -1;


    @Override
    public LoanType getLoanType() {
        return LoanType.MORTGAGE;
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
