package com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculator;
import com.myZipPlan.server.calculator.dsrCalc.domain.DsrCalcResult;
import com.myZipPlan.server.calculator.dsrCalc.dto.DsrCalcServiceRequest;
import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.myZipPlan.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.myZipPlan.server.common.enums.calculator.LoanType;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class MortgageLoanDsrCalc implements DsrCalculator {

    @Autowired
    RepaymentCalcService repaymentCalcService;

    private static final BigDecimal MAX_TERM_FOR_BULLET = BigDecimal.valueOf(120);
    private static final BigDecimal MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = BigDecimal.valueOf(-1);

    @Override
    public LoanType getLoanType() {
        return LoanType.MORTGAGE;
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
            BigDecimal maxTermForBullet = getMaxTermForBullet();
            BigDecimal actualTerm = loanStatus.getTerm();
            BigDecimal term = maxTermForBullet.min(actualTerm);
            dsrCalcResult = dsrCommonCaclulator.dsrCalcForBulletLoan(loanStatus, term);
        }
        if (repaymentType == RepaymentType.AMORTIZING) {
            dsrCalcResult = dsrCalcForInstallmentRepaymentMortgageLoan(loanStatus);
        }
        if (repaymentType == RepaymentType.EQUAL_PRINCIPAL) {
            dsrCalcResult = dsrCalcForInstallmentRepaymentMortgageLoan(loanStatus);
        }
        return dsrCalcResult;
    }

    /***
     *
     * @param loanStatus
     * @return DsrCalcResult
     *
     * 연원금상환액 = 분할상환액 + 만기상환액 / (대출기간 - 거치기간)
     *  ㄴ 분할상환액 = (대출총액 - 만기상환액) / 대출기간
     *  연이자상환액 = 총이자액 / 대출기간 * 12
     */
    private DsrCalcResult dsrCalcForInstallmentRepaymentMortgageLoan(DsrCalcServiceRequest.LoanStatus loanStatus) {
        BigDecimal principal = loanStatus.getPrincipal();
        BigDecimal maturityPaymentAmount = loanStatus.getMaturityPaymentAmount();
        BigDecimal term = loanStatus.getTerm();
        BigDecimal gracePeriod = loanStatus.getGracePeriod();
        BigDecimal actualRepaymentTerm = term.subtract( gracePeriod);

        BigDecimal installmentRepayment = (principal.subtract(maturityPaymentAmount)).divide(term, 4, RoundingMode.DOWN).multiply(BigDecimal.valueOf(12));
        BigDecimal maturityRepayment = maturityPaymentAmount.divide(actualRepaymentTerm, 4, RoundingMode.DOWN);
        BigDecimal annualPrincipalRepayment = installmentRepayment.add(maturityRepayment);

        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculate(loanStatus.toRepaymentCalcServiceRequest());
        BigDecimal totalInterest = repaymentCalcResponse.getTotalInterest();
        BigDecimal annalInterestRepayment = totalInterest.divide(term, 4, RoundingMode.DOWN).multiply(BigDecimal.valueOf(12));
        return DsrCalcResult.builder()
            .principal(principal)
            .term(term)
            .annualPrincipalRepayment(annualPrincipalRepayment)
            .annualInterestRepayment(annalInterestRepayment)
            .build();
    }
}
