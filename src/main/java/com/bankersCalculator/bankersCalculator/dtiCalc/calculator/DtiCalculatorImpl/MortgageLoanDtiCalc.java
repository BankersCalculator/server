package com.bankersCalculator.bankersCalculator.dtiCalc.calculator.DtiCalculatorImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.common.enums.RepaymentType;
import com.bankersCalculator.bankersCalculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcServiceRequest;
import com.bankersCalculator.bankersCalculator.dtiCalc.calculator.DtiCalculator;
import com.bankersCalculator.bankersCalculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcServiceRequest;
import com.bankersCalculator.bankersCalculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.bankersCalculator.bankersCalculator.repaymentCalc.service.RepaymentCalcService;
/* 프로그램 설명 : MortgageLoanDtiCalc 클래스는 LoanType.MORTGAGE에 대한 DtiCalculator 구현체를 제공합
 * 구현체 (Implementation): 구현체는 인터페이스에서 정의한 메서드를 실제로 구현하는 클래스를 말합니다. 즉, 인터페이스의 계약(메서드 시그니처)을 준수하면서 그 동작을 실제로 제공하는 클래스입니다.
 * 
 */

//스프링 컨텍스트에 빈으로 등록 
@Component
public class MortgageLoanDtiCalc implements DtiCalculator{
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
    public DtiCalcResult calculateDti(DtiCalcServiceRequest.LoanStatus loanStatus) {
        RepaymentType repaymentType = loanStatus.getRepaymentType();
        DtiCalcResult dtiCalcResult = DtiCalcResult.builder().build();

        if (repaymentType == RepaymentType.BULLET) {
            int maxTermForBullet = getMaxTermForBullet();
            int actualTerm = loanStatus.getTerm();
            int term = Math.min(maxTermForBullet, actualTerm);
            dtiCalcResult = dtiCommonCalculator.dtiCalcForBulletLoan(loanStatus, term);
        }
        if (repaymentType == RepaymentType.AMORTIZING) {
            dtiCalcResult = dtiCalcForInstallmentRepaymentMortgageLoan(loanStatus);
        }
        if (repaymentType == RepaymentType.EQUAL_PRINCIPAL) {
            dtiCalcResult = dtiCalcForInstallmentRepaymentMortgageLoan(loanStatus);
        }
        return dtiCalcResult;
    }
    
    private DtiCalcResult dtiCalcForInstallmentRepaymentMortgageLoan(DtiCalcServiceRequest.LoanStatus loanStatus) {
        double principal = loanStatus.getPrincipal();
        double maturityPaymentAmount = loanStatus.getMaturityPaymentAmount();
        int term = loanStatus.getTerm();
        int gracePeriod = loanStatus.getGracePeriod();
        int actualRepaymentTerm = term - gracePeriod;

        double installmentRepayment = (principal - maturityPaymentAmount) / term * 12;
        double maturityRepayment = maturityPaymentAmount / actualRepaymentTerm;
        double annualPrincipalRepayment = installmentRepayment + maturityRepayment;

        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(loanStatus.toRepaymentCalcServiceRequest());
        double totalInterest = repaymentCalcResponse.getTotalInterest();
        double annalInterestRepayment = totalInterest / term * 12;

        return DtiCalcResult.builder()
            .principal(principal)
            .term(term)
            .annualPrincipalRepayment(annualPrincipalRepayment)
            .annualInterestRepayment(annalInterestRepayment)
            .build();
    }

}
