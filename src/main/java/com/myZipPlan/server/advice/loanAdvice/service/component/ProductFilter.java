package com.myZipPlan.server.advice.loanAdvice.service.component;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductFilter {

    private final List<LoanProduct> loanProducts;

    public List<FilterProductResultDto> filterProduct(LoanAdviceServiceRequest request) {

        // 전세대출 상품들을 불러와서 필터링을 수행
        List<FilterProductResultDto> result = new ArrayList<>();

        for (LoanProduct loanProduct : loanProducts) {
            FilterProductResultDto filteringResult = loanProduct.filtering(request);
            result.add(filteringResult);
        }

        return result;
    }

    public List<FilterProductResultDto> filterSpecificProduct(LoanAdviceServiceRequest request) {

        String productCode = request.getSpecificRequestProductCode();
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
