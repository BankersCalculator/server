package com.myZipPlan.server.advice.loanAdvice.service.component;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.LoanTermsResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProduct;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProductFactory;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseLoanProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class LoanTermCalculator {

    private final LoanProductFactory loanProductFactory;

    public List<LoanTermsResultDto> calculateLoanTerms(LoanAdviceServiceRequest request,
                                                       List<FilterProductResultDto> filteredProducts) {

        List<LoanTermsResultDto> result = new ArrayList<>();

        for (FilterProductResultDto filteredProduct : filteredProducts) {
            LoanProduct loanProduct = loanProductFactory.getLoanProduct(filteredProduct.getProductType());
            LoanTermsResultDto loanLimitAndRateResult = loanProduct.calculateLoanTerms(request);
            loanLimitAndRateResult.setIsEligible(filteredProduct.isEligible());
            result.add(loanLimitAndRateResult);
        }
        return result;
    }

    // 간편조회용: 모든 상품에 대해 최대 한도 및 최저 금리 계산
    public List<LoanTermsResultDto> calculateSimpleLoanTerms(BigDecimal rentalDeposit) {
        List<LoanProduct> loanProducts = loanProductFactory.getAllLoanProducts();
        List<LoanTermsResultDto> result = new ArrayList<>();
        for (LoanProduct loanProduct : loanProducts) {
            LoanTermsResultDto term = loanProduct.calculateMaxLoanLimitAndMinRate(rentalDeposit);
            result.add(term);
        }
        return result;
    }

    // 특정 상품에 대한 최대 한도/최저 금리 계산 (필요 시)
    public LoanTermsResultDto calculateMaxLoanTermForProduct(JeonseLoanProductType productType) {
        LoanProduct loanProduct = loanProductFactory.getLoanProduct(productType);
        return loanProduct.calculateMaxLoanLimitAndMinRate(BigDecimal.valueOf(9999999999L));
    }
}
