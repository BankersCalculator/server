package com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculator;
import org.springframework.stereotype.Component;

@Component
public class JeonseLoanDsrCalc implements DsrCalculator {

    private static final int MAX_TERM_FOR_BULLET = 1;
    private static final int MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = 1;  // TODO: 수정할 것..

    // TODO: 전세는 원금 상환액을 안 차감함.. 구현을 따로 해야하나?

    @Override
    public LoanType getLoanType() {
        return LoanType.JEONSE_LOAN;
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
