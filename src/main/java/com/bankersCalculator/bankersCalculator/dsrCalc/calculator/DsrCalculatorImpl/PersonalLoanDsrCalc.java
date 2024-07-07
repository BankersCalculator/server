package com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.common.enums.RepaymentType;
import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.CommonCalculator;
import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculator;
import com.bankersCalculator.bankersCalculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class PersonalLoanDsrCalc implements DsrCalculator {

    private static final int MAX_TERM_FOR_BULLET = 60;
    private static final int MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = 120;

    private CommonCalculator commonCalculator;

    @Override
    public LoanType getLoanType() {
        return LoanType.PERSONAL_LOAN;
    }

    /**
     * 1. 일시상환
     * 2. 원금균등/원리금균등 - 전체 기준
     * 3. 원금균등/원리금균등 - 초년도 기준
     */
    @Override
    public DsrCalcResult calculateDsr(DsrCalcServiceRequest.LoanStatus loanStatus) {

        RepaymentType repaymentType = loanStatus.getRepaymentType();

        DsrCalcResult dsrCalcResult= DsrCalcResult.builder().build();
        if (repaymentType == RepaymentType.BULLET) {
            dsrCalcResult = commonCalculator.calcForBulletLoan(loanStatus, MAX_TERM_FOR_BULLET);
        }

        return dsrCalcResult;
    }
}
