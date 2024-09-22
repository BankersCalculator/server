package com.myZipPlan.server.calculator.dsrCalc.calculator;

import com.myZipPlan.server.calculator.dsrCalc.domain.DsrCalcResult;
import com.myZipPlan.server.calculator.dsrCalc.dto.DsrCalcServiceRequest;
import com.myZipPlan.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.myZipPlan.server.common.enums.calculator.LoanType;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public interface DsrCalculator {

    @Autowired
    DsrCommonCalculator dsrCommonCaclulator = new DsrCommonCalculator(new RepaymentCalcService());

    LoanType getLoanType();

    BigDecimal getMaxTermForBullet();

    BigDecimal getMaxTermForEqualPrincipalAndAmortizing();

    default DsrCalcResult calculateDsr(DsrCalcServiceRequest.LoanStatus loanStatus) {
        RepaymentType repaymentType = loanStatus.getRepaymentType();
        DsrCalcResult dsrCalcResult = DsrCalcResult.builder().build();

        if (repaymentType == RepaymentType.BULLET) {
            BigDecimal maxTermForBullet = getMaxTermForBullet();
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForBulletLoan(loanStatus, maxTermForBullet);
        }
        if (repaymentType == RepaymentType.AMORTIZING) {
            BigDecimal maxTermForAmortizing = getMaxTermForEqualPrincipalAndAmortizing();
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForAmortizingLoan(loanStatus, maxTermForAmortizing);
        }
        if (repaymentType == RepaymentType.EQUAL_PRINCIPAL) {
            BigDecimal maxTermForEqualPrincipal = getMaxTermForEqualPrincipalAndAmortizing();
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForEqualPrincipalLoan(loanStatus, maxTermForEqualPrincipal);
        }

        return dsrCalcResult;
    }
}
