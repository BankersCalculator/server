package com.myZipPlan.server.advice.loanAdvice.service.component;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProduct;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProductFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class LoanLimitAndRateCalculator {

    private final LoanProductFactory loanProductFactory;

    public List<LoanLimitAndRateResultDto> calculateLoanLimitAndRate(LoanAdviceServiceRequest request,
                                                                     List<FilterProductResultDto> filteredProducts) {

        List<LoanLimitAndRateResultDto> result = new ArrayList<>();

        for (FilterProductResultDto filteredProduct : filteredProducts) {
            LoanProduct loanProduct = loanProductFactory.getLoanProduct(filteredProduct.getProductType());
            LoanLimitAndRateResultDto loanLimitAndRateResult = loanProduct.calculateLoanLimitAndRate(request);
            loanLimitAndRateResult.setIsEligible(filteredProduct.isEligible());
            result.add(loanLimitAndRateResult);
        }

        return result;
    }

    public List<LoanLimitAndRateResultDto> calculateMaxLoanLimitAndMinRate(BigDecimal rentalAmount) {

        List<LoanProduct> loanProducts = loanProductFactory.getAllLoanProducts();
        List<LoanLimitAndRateResultDto> result = new ArrayList<>();

        for (LoanProduct loanProduct : loanProducts) {
            LoanLimitAndRateResultDto loanLimitAndRateResult = loanProduct.calculateMaxLoanLimitAndMinRate(rentalAmount);
            result.add(loanLimitAndRateResult);
        }

        return result;
    }
}
