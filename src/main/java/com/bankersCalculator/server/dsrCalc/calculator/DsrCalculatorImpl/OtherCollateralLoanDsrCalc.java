package com.bankersCalculator.server.dsrCalc.calculator.DsrCalculatorImpl;

import com.bankersCalculator.server.common.enums.LoanType;
import com.bankersCalculator.server.dsrCalc.calculator.DsrCalculator;
import org.springframework.stereotype.Component;

@Component
public class OtherCollateralLoanDsrCalc implements DsrCalculator {

    private static final int MAX_TERM_FOR_BULLET = 120;
    private static final int MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = 120;

    @Override
    public LoanType getLoanType() {
        return LoanType.OTHER_COLLATERAL_LOAN;
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
