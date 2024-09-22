package com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculator;
import com.myZipPlan.server.calculator.dsrCalc.domain.DsrCalcResult;
import com.myZipPlan.server.calculator.dsrCalc.dto.DsrCalcServiceRequest;
import com.myZipPlan.server.common.enums.calculator.LoanType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DepositAndInsuranceCollateralLoanDsrCalc implements DsrCalculator {

    private static final BigDecimal MAX_TERM_FOR_BULLET = BigDecimal.valueOf(-1);
    private static final BigDecimal MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = BigDecimal.valueOf(-1);

    @Override
    public LoanType getLoanType() {
        return LoanType.DEPOSIT_AND_INSURANCE_COLLATERAL_LOAN;
    }

    @Override
    public BigDecimal getMaxTermForBullet() {
        return MAX_TERM_FOR_BULLET;
    }

    @Override
    public BigDecimal getMaxTermForEqualPrincipalAndAmortizing() {
        return MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING;
    }

    @Override
    public DsrCalcResult calculateDsr(DsrCalcServiceRequest.LoanStatus loanStatus) {
        return dsrCommonCaclulator.dsrCalcWithoutPrincipalRepayment(loanStatus);
    }
}
