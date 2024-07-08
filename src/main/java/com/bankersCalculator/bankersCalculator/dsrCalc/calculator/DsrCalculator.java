package com.bankersCalculator.bankersCalculator.dsrCalc.calculator;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.common.enums.RepaymentType;
import com.bankersCalculator.bankersCalculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;

public interface DsrCalculator {

    @Autowired
    DsrCommonCalculator dsrCalcForBulletLoan = new DsrCommonCalculator();

    LoanType getLoanType();

    int getMaxTermForBullet();
    int getMaxTermForEqualPrincipalAndAmortizing();

    default DsrCalcResult calculateDsr(DsrCalcServiceRequest.LoanStatus loanStatus) {
        RepaymentType repaymentType = loanStatus.getRepaymentType();
        DsrCalcResult dsrCalcResult = DsrCalcResult.builder().build();

        if (repaymentType == RepaymentType.BULLET) {
            int maxTermForBullet = getMaxTermForBullet();
            dsrCalcResult = dsrCalcForBulletLoan.dsrCalcForBulletLoan(loanStatus, maxTermForBullet);
        } else {
            int maxTermForEqualPrincipalAndAmortizing = getMaxTermForEqualPrincipalAndAmortizing();

            // TODO: 메서드 변경할것
//            dsrCalcResult = dsrCalcForBulletLoan.dsrCalcForBulletLoan(loanStatus, maxTermForEqualPrincipalAndAmortizing);
        }
        return dsrCalcResult;
    }
}
