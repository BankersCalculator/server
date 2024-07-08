package com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.common.enums.RepaymentType;
import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculator;
import com.bankersCalculator.bankersCalculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class DepositAndInsuranceCollateralLoanDsrCalc implements DsrCalculator {

    private static final int MAX_TERM_FOR_BULLET = -1;
    private static final int MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = -1;

    @Override
    public DsrCalcResult calculateDsr(DsrCalcServiceRequest.LoanStatus loanStatus) {
        RepaymentType repaymentType = loanStatus.getRepaymentType();
        DsrCalcResult dsrCalcResult = DsrCalcResult.builder().build();

        if (repaymentType == RepaymentType.BULLET) {
            int maxTermForBullet = getMaxTermForBullet();
            dsrCalcResult = dsrCalcForBulletLoan.dsrCalcForBulletLoanWithoutPrincipalRepayment(loanStatus);
        }

        return dsrCalcResult;
    }

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
