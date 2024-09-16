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

@Component
public class MortgageLoanDsrCalc implements DsrCalculator {

    @Autowired
    RepaymentCalcService repaymentCalcService;

    private static final int MAX_TERM_FOR_BULLET = 120;
    private static final int MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = -1;

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

    @Override
    public DsrCalcResult calculateDsr(DsrCalcServiceRequest.LoanStatus loanStatus) {
        RepaymentType repaymentType = loanStatus.getRepaymentType();
        DsrCalcResult dsrCalcResult = DsrCalcResult.builder().build();

        if (repaymentType == RepaymentType.BULLET) {
            int maxTermForBullet = getMaxTermForBullet();
            int actualTerm = loanStatus.getTerm();
            int term = Math.min(maxTermForBullet, actualTerm);
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
        double principal = loanStatus.getPrincipal();
        double maturityPaymentAmount = loanStatus.getMaturityPaymentAmount();
        int term = loanStatus.getTerm();
        int gracePeriod = loanStatus.getGracePeriod();
        int actualRepaymentTerm = term - gracePeriod;

        double installmentRepayment = (principal - maturityPaymentAmount) / term * 12;
        double maturityRepayment = maturityPaymentAmount / actualRepaymentTerm;
        double annualPrincipalRepayment = installmentRepayment + maturityRepayment;

        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculate(loanStatus.toRepaymentCalcServiceRequest());
        double totalInterest = repaymentCalcResponse.getTotalInterest();
        double annalInterestRepayment = totalInterest / term * 12;

        return DsrCalcResult.builder()
            .principal(principal)
            .term(term)
            .annualPrincipalRepayment(annualPrincipalRepayment)
            .annualInterestRepayment(annalInterestRepayment)
            .build();
    }
}
