package com.bankersCalculator.server.calculator.dsrCalc.calculator;

import com.bankersCalculator.server.calculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.server.calculator.dsrCalc.dto.DsrCalcServiceRequest;
import com.bankersCalculator.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.bankersCalculator.server.common.enums.LoanType;
import com.bankersCalculator.server.common.enums.RepaymentType;
import org.springframework.beans.factory.annotation.Autowired;

public interface DsrCalculator {

    @Autowired
    DsrCommonCalculator dsrCommonCaclulator = new DsrCommonCalculator(new RepaymentCalcService());

    LoanType getLoanType();

    int getMaxTermForBullet();

    int getMaxTermForEqualPrincipalAndAmortizing();

    default DsrCalcResult calculateDsr(DsrCalcServiceRequest.LoanStatus loanStatus) {
        RepaymentType repaymentType = loanStatus.getRepaymentType();
        DsrCalcResult dsrCalcResult = DsrCalcResult.builder().build();

        if (repaymentType == RepaymentType.BULLET) {
            int maxTermForBullet = getMaxTermForBullet();
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForBulletLoan(loanStatus, maxTermForBullet);
        }
        if (repaymentType == RepaymentType.AMORTIZING) {
            int maxTermForAmortizing = getMaxTermForEqualPrincipalAndAmortizing();
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForAmortizingLoan(loanStatus, maxTermForAmortizing);
        }
        if (repaymentType == RepaymentType.EQUAL_PRINCIPAL) {
            int maxTermForEqualPrincipal = getMaxTermForEqualPrincipalAndAmortizing();
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForEqualPrincipalLoan(loanStatus, maxTermForEqualPrincipal);
        }

        return dsrCalcResult;
    }
}
