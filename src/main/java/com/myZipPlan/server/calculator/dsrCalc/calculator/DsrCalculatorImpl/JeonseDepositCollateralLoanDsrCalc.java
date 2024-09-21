package com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculator;
import com.myZipPlan.server.common.enums.calculator.LoanType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JeonseDepositCollateralLoanDsrCalc implements DsrCalculator {

    private static final BigDecimal MAX_TERM_FOR_BULLET = BigDecimal.valueOf(48);
    private static final BigDecimal MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = BigDecimal.valueOf(48);

    @Override
    public LoanType getLoanType() {
        return LoanType.JEONSE_DEPOSIT_COLLATERAL_LOAN;
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
