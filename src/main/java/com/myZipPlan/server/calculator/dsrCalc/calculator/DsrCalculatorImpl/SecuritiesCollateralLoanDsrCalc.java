package com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculator;
import com.myZipPlan.server.common.enums.calculator.LoanType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SecuritiesCollateralLoanDsrCalc implements DsrCalculator {

    private static final BigDecimal MAX_TERM_FOR_BULLET = BigDecimal.valueOf(96);
    private static final BigDecimal MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = BigDecimal.valueOf(96);

    @Override
    public LoanType getLoanType() {
        return LoanType.SECURITIES_COLLATERAL_LOAN;
    }

    @Override
    public BigDecimal getMaxTermForBullet() {
        return MAX_TERM_FOR_BULLET;
    }

    @Override
    public BigDecimal getMaxTermForEqualPrincipalAndAmortizing() {
        return MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING;
    }
}
