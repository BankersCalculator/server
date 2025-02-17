package com.myZipPlan.server.advice.loanAdvice.service.component;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.BestLoanProductResult;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.LoanTermsResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.response.RecommendedProductDto;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseLoanProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class LoanRecommendationAssembler {

    // 최적 상품을 제외한 추천 상품 목록 생성
    public List<RecommendedProductDto> assembleRecommendedProducts(List<FilterProductResultDto> filterResults,
                                                                   List<LoanTermsResultDto> loanTerms,
                                                                   BestLoanProductResult bestProduct) {
        List<RecommendedProductDto> recommendedProducts = new ArrayList<>();
        Map<JeonseLoanProductType, LoanTermsResultDto> termMap = loanTerms.stream()
            .collect(Collectors.toMap(LoanTermsResultDto::getProductType, Function.identity()));

        for (FilterProductResultDto filterResult : filterResults) {
            JeonseLoanProductType productType = filterResult.getProductType();
            LoanTermsResultDto loanTerm = termMap.get(productType);
            if (productType.equals(bestProduct.getProductType())) {
                continue;
            }
            RecommendedProductDto recommendedProduct = createRecommendedProduct(filterResult, productType, loanTerm);
            recommendedProducts.add(recommendedProduct);
        }
        return recommendedProducts;
    }

    // 부적합 상품의 경우, 부적합 사유를 포함한 DTO 생성
    public List<RecommendedProductDto> assembleIneligibleProducts(List<FilterProductResultDto> filterResults) {
        List<RecommendedProductDto> recommendedProducts = new ArrayList<>();
        for (FilterProductResultDto filterResult : filterResults) {
            JeonseLoanProductType productType = filterResult.getProductType();
            RecommendedProductDto recommendedProduct = RecommendedProductDto.create(
                productType.getProductName(),
                productType.getProductCode(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                filterResult.getNotEligibleReasons());
            recommendedProducts.add(recommendedProduct);
        }
        return recommendedProducts;
    }


    private RecommendedProductDto createRecommendedProduct(FilterProductResultDto filterResult,
                                                           JeonseLoanProductType productType,
                                                           LoanTermsResultDto loanTerm) {
        BigDecimal loanLimit = loanTerm.getPossibleLoanLimit() == null ? BigDecimal.ZERO : loanTerm.getPossibleLoanLimit();
        BigDecimal expectedLoanRate = loanTerm.getExpectedLoanRate() == null ? BigDecimal.ZERO : loanTerm.getExpectedLoanRate();
        return RecommendedProductDto.create(
            productType.getProductName(),
            productType.getProductCode(),
            loanLimit,
            expectedLoanRate,
            filterResult.getNotEligibleReasons()
        );
    }
}
