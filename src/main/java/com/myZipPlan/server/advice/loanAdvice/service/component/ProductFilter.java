package com.myZipPlan.server.advice.loanAdvice.service.component;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProduct;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProductFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductFilter {

    private final LoanProductFactory loanProductFactory;

    public List<FilterProductResultDto> filterProduct(LoanAdviceServiceRequest request) {

        // 전세대출 상품들을 불러와서 필터링을 수행
        List<LoanProduct> loanProducts = loanProductFactory.getAllLoanProducts();
        List<FilterProductResultDto> result = new ArrayList<>();

        for (LoanProduct loanProduct : loanProducts) {
            FilterProductResultDto filteringResult = loanProduct.filtering(request);
            result.add(filteringResult);
        }

        return result;
    }

    // 한도산출 등은 동일하기 때문에 동일하게 모든 상품을 담는다. 대상 상품이 eligible false 인 경우 예외처리
    public List<FilterProductResultDto> filterSpecificProduct(LoanAdviceServiceRequest request) {

        String productCode = request.getSpecificRequestProductCode();
        List<LoanProduct> loanProducts = loanProductFactory.getAllLoanProducts();
        List<FilterProductResultDto> result = new ArrayList<>();

        for (LoanProduct loanProduct : loanProducts) {
            FilterProductResultDto filteringResult = loanProduct.filtering(request);
            if (filteringResult.getProductType().getProductCode().equals(productCode)) {
                if (!filteringResult.isEligible()){
                    throw new IllegalArgumentException("해당 상품은 대출 가능한 상품이 아닙니다.");
                }
            }
            result.add(filteringResult);
        }

        return result;
    }

}
