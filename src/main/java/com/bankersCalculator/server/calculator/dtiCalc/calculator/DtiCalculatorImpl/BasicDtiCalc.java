package com.bankersCalculator.server.calculator.dtiCalc.calculator.DtiCalculatorImpl;

import com.bankersCalculator.server.calculator.dtiCalc.calculator.DtiCalculator;
import com.bankersCalculator.server.calculator.dtiCalc.calculator.DtiCommonCalculator;
import com.bankersCalculator.server.calculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcServiceRequest;
import com.bankersCalculator.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.bankersCalculator.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.bankersCalculator.server.common.enums.LoanType;
import com.bankersCalculator.server.common.enums.RepaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/* 프로그램 설명 : MortgageLoanDtiCalc 클래스는 LoanType.MORTGAGE에 대한 DtiCalculator 구현체를 제공합
 * 구현체 (Implementation): 구현체는 인터페이스에서 정의한 메서드를 실제로 구현하는 클래스를 말합니다. 즉, 인터페이스의 계약(메서드 시그니처)을 준수하면서 그 동작을 실제로 제공하는 클래스입니다.
 *
 */

//스프링 컨텍스트에 빈으로 등록 
@Component
public class BasicDtiCalc implements DtiCalculator {
    
    @Autowired
    DtiCommonCalculator dtiCommonCaclulator = new DtiCommonCalculator(new RepaymentCalcService());

    @Autowired
    RepaymentCalcService repaymentCalcService;

    private static final int MAX_TERM_FOR_BULLET = 120;
    private static final int MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING = -1;


    public int getMaxTermForBullet() {
        return MAX_TERM_FOR_BULLET;
    }

    public int getMaxTermForEqualPrincipalAndAmortizing() {
        return MAX_TERM_FOR_EQUALPRINCIPAL_AND_AMORTIZING;
    }

    @Override
    public DtiCalcResult calculateDti(DtiCalcServiceRequest.LoanStatus loanStatus) {
        RepaymentType repaymentType = loanStatus.getRepaymentType();
        DtiCalcResult dtiCalcResult = DtiCalcResult.builder().build();

        // 주택담보대출인 경우
        if (loanStatus.getLoanType() == LoanType.MORTGAGE) {
        	
            if (repaymentType == RepaymentType.BULLET) {
                int maxTermForBullet = getMaxTermForBullet();
                int actualTerm = loanStatus.getTerm();
                int term = Math.min(maxTermForBullet, actualTerm);
                dtiCalcResult = dtiCommonCaclulator.dtiCalcForBulletLoan(loanStatus, term);
                
            }
            if (repaymentType == RepaymentType.AMORTIZING) {
                dtiCalcResult = dtiCalcForInstallmentRepaymentMortgageLoan(loanStatus);
            }
            if (repaymentType == RepaymentType.EQUAL_PRINCIPAL) {
                dtiCalcResult = dtiCalcForInstallmentRepaymentMortgageLoan(loanStatus);
            }
            return dtiCalcResult;

            //나머지 대출에 대해서는 연이자만 계함.
        } else {
            double principal = loanStatus.getPrincipal();
            double interestRatePercentage = loanStatus.getInterestRate();
            double annalInterestRepayment = principal * interestRatePercentage;
            dtiCalcResult = DtiCalcResult.builder()
                .principal(principal)
                .annualInterestRepayment(annalInterestRepayment)
                .build();
            return dtiCalcResult;
        }
    }

    /***
     *
     * @param loanStatus
     * @return DtiCalcResult
     *
     * 연원금상환액 = 분할상환액 + 만기상환액 / (대출기간 - 거치기간)
     *  ㄴ 분할상환액 = (대출총액 - 만기상환액) / 대출기간
     *  연이자상환액 = 총이자액 / 대출기간 * 12
     */
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
