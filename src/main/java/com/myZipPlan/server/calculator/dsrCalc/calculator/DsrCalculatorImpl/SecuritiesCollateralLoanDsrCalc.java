package com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculator;
import com.myZipPlan.server.common.enums.calculator.LoanType;
import org.springframework.stereotype.Component;

@Component
public class SecuritiesCollateralLoanDsrCalc implements DsrCalculator {

    private static final int MAX_TERM_FOR_BULLET = 96;
    private static final int MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = 96;

    @Override
    public LoanType getLoanType() {
        return LoanType.SECURITIES_COLLATERAL_LOAN;
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
