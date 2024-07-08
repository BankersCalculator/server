package com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculator;
import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCommonCalculator;
import com.bankersCalculator.bankersCalculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class MortgageLoanDsrCalc implements DsrCalculator {

    private static final int MAX_TERM_FOR_BULLET = 60;  // TODO: 수정할 것...
    private static final int MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = 120; // TODO: 수정할 것...


    // TODO: 구현할 것..
    @Override
    public DsrCalcResult calculateDsr(DsrCalcServiceRequest.LoanStatus loanStatus) {
        return DsrCalculator.super.calculateDsr(loanStatus);
    }

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
