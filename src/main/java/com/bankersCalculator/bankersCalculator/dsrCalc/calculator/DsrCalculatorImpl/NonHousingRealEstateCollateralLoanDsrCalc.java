package com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculator;
import org.springframework.stereotype.Component;

@Component
public class NonHousingRealEstateCollateralLoanDsrCalc implements DsrCalculator {

    private static final int MAX_TERM_FOR_BULLET = 60;
    private static final int MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = 120;

    @Override
    public LoanType getLoanType() {
        return LoanType.NON_HOUSING_REAL_ESTATE_COLLATERAL_LOAN;
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
