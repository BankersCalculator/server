package com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculator;
import org.springframework.stereotype.Component;

@Component
public class DepositAndInsuranceCollateralLoanDsrCalc implements DsrCalculator {

    private static final int MAX_TERM_FOR_BULLET = 1;
    private static final int MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = 1;

    // TODO: 예담대/보험은 원금 상환액을 안 차감함.. 구현을 따로 해야하나?


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
