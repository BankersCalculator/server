package com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculator;
import com.myZipPlan.server.common.enums.calculator.LoanType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class InterimPaymentAndMovingLoanDsrCalc implements DsrCalculator {

    private static final BigDecimal MAX_TERM_FOR_BULLET = BigDecimal.valueOf(300);
    private static final BigDecimal MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = BigDecimal.valueOf(300);

    @Override
    public LoanType getLoanType() {
        return LoanType.INTERIM_PAYMENT_AND_MOVING;
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
