package com.bankersCalculator.server.advice.loanAdvice.service.component;


import com.bankersCalculator.server.advice.loanAdvice.dto.internal.AdditionalInformation;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.BestLoanProductResult;
import com.bankersCalculator.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.model.LoanProduct;
import com.bankersCalculator.server.advice.loanAdvice.model.LoanProductFactory;
import com.bankersCalculator.server.common.enums.Bank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequiredArgsConstructor
@Component
public class AdditionalInfoGenerator {

    private final LoanProductFactory loanProductFactory;

    public AdditionalInformation generateAdditionalInfo(LoanAdviceServiceRequest request,
                                                        BestLoanProductResult optimalLoanProduct) {

        // 소요자기자금
        BigDecimal rentalDeposit = request.getRentalDeposit();
        BigDecimal loanAmount = optimalLoanProduct.getPossibleLoanLimit();
        BigDecimal ownFunds = rentalDeposit.subtract(loanAmount);

        // 월이자비용
        BigDecimal monthlyInterestCost = loanAmount.multiply(optimalLoanProduct.getExpectedLoanRate()
            .divide(BigDecimal.valueOf(100))).divide(BigDecimal.valueOf(12), 0, BigDecimal.ROUND_HALF_UP);

        // 월 임대료
        BigDecimal monthlyRent = request.getMonthlyRent();

        // 총 주거 비용
        BigDecimal totalLivingCost = monthlyInterestCost.add(request.getMonthlyRent());

        // 예금 이자율
        BigDecimal depositInterestRate = BigDecimal.valueOf(0.03);
        // 기회비용
        BigDecimal opportunityCostOwnFunds = ownFunds.multiply(depositInterestRate.divide(BigDecimal.valueOf(12), 0, BigDecimal.ROUND_HALF_UP));

        // 보증보험료
        LoanProduct loanProduct = loanProductFactory.getLoanProduct(optimalLoanProduct.getProductType());
        BigDecimal guaranteeInsuranceFee = loanProduct.getGuaranteeInsuranceFee(loanAmount);

        // 인지세
        BigDecimal stampDuty = calculateStampDuty(loanAmount);

        // 취급 가능 은행
        List<Bank> availableBanks = loanProduct.getAvailableBanks();

        return AdditionalInformation.builder()
            .ownFunds(ownFunds)
            .monthlyInterestCost(monthlyInterestCost)
            .totalLivingCost(totalLivingCost)
            .monthlyRent(monthlyRent)
            .opportunityCostOwnFunds(opportunityCostOwnFunds)
            .depositInterestRate(depositInterestRate)
            .guaranteeInsuranceFee(guaranteeInsuranceFee)
            .stampDuty(stampDuty)
            .availableBanks(availableBanks)
            .rentalLoanGuide("전세대출 가이드")    // TODO: 어떻게 구현하는 게 좋을지.. 고민 필요
            .build();
    }

    public BigDecimal calculateStampDuty(BigDecimal loanAmount) {
        BigDecimal stampDuty;
        if (loanAmount.compareTo(new BigDecimal("50000000")) <= 0) {
            stampDuty = BigDecimal.ZERO;
        } else if (loanAmount.compareTo(new BigDecimal("100000000")) <= 0) {
            stampDuty = new BigDecimal("70000");
        } else if (loanAmount.compareTo(new BigDecimal("1000000000")) <= 0) {
            stampDuty = new BigDecimal("150000");
        } else {
            stampDuty = new BigDecimal("350000");
        }
        // 고객과 은행이 각각 50%씩 부담
        return stampDuty.divide(new BigDecimal("2"), 0, RoundingMode.HALF_UP);
    }
}
