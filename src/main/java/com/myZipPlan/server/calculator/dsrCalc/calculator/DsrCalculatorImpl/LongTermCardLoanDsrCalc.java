package com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculator;
import com.myZipPlan.server.calculator.dsrCalc.domain.DsrCalcResult;
import com.myZipPlan.server.calculator.dsrCalc.dto.DsrCalcServiceRequest;
import com.myZipPlan.server.common.enums.calculator.LoanType;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LongTermCardLoanDsrCalc implements DsrCalculator {

    private static final BigDecimal MAX_TERM_FOR_BULLET = BigDecimal.valueOf(36);
    private static final BigDecimal MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = BigDecimal.valueOf(60);

    @Override
    public LoanType getLoanType() {
        return LoanType.LONG_TERM_CARD_LOAN;
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
        RepaymentType repaymentType = loanStatus.getRepaymentType();
        DsrCalcResult dsrCalcResult = DsrCalcResult.builder().build();

        if (repaymentType == RepaymentType.BULLET) {
            BigDecimal term = loanStatus.getTerm().min(getMaxTermForBullet());
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForBulletLoan(loanStatus, term);
        }
        if (repaymentType == RepaymentType.AMORTIZING) {
            BigDecimal term = loanStatus.getTerm().min(getMaxTermForEqualPrincipalAndAmortizing());
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForAmortizingLoan(loanStatus, term);
        }
        if (repaymentType == RepaymentType.EQUAL_PRINCIPAL) {
            BigDecimal term = loanStatus.getTerm().min(getMaxTermForEqualPrincipalAndAmortizing());
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForEqualPrincipalLoan(loanStatus, term);
        }
        return dsrCalcResult;
    }
}
