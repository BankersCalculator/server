package com.myZipPlan.server.advice.loanAdvice.service;

import com.myZipPlan.server.advice.aiReportGenerator.service.AiReportService;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.AdditionalInformation;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.BestLoanProductResult;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.LoanTermsResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.myZipPlan.server.advice.loanAdvice.dto.response.RecommendedProductDto;
import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.myZipPlan.server.advice.loanAdvice.service.component.AdditionalInfoGenerator;
import com.myZipPlan.server.advice.loanAdvice.service.component.LoanTermCalculator;
import com.myZipPlan.server.advice.loanAdvice.service.component.ProductComparator;
import com.myZipPlan.server.advice.loanAdvice.service.component.ProductFilter;
import com.myZipPlan.server.advice.userInputInfo.entity.UserInputInfo;
import com.myZipPlan.server.advice.userInputInfo.service.UserInputInfoService;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class LoanAdviceOrchestrator {

    private final ProductFilter productFilter;
    private final LoanTermCalculator loanTermCalculator;
    private final ProductComparator productComparator;
    private final AdditionalInfoGenerator additionalInfoGenerator;
    private final LoanRecommendationAssembler loanRecommendationAssembler;

    private final AiReportService aiReportService;
    private final UserService userService;
    private final UserInputInfoService userInputInfoService;
    private final LoanAdviceResultRepository loanAdviceResultRepository;

    public LoanAdviceResponse processLoanAdvice(LoanAdviceServiceRequest request) {
        // 1. 상품 필터링
        List<FilterProductResultDto> filterResults = productFilter.filterProduct(request);
        if (!hasEligibleProducts(filterResults)) {
            List<RecommendedProductDto> recommendedProducts = loanRecommendationAssembler.assembleIneligibleProducts(filterResults);
            return LoanAdviceResponse.ofEmpty(recommendedProducts);
        }

        // 2. 한도 및 금리 계산
        List<LoanTermsResultDto> loanTerms = loanTermCalculator.calculateLoanTerms(request, filterResults);

        // 3. 최적 상품 선정
        BestLoanProductResult bestProduct = productComparator.compareProducts(
            request.getRentalDeposit(),
            request.getSpecificRequestProductCode(),
            loanTerms);

        // 4. 추가 정보 생성
        AdditionalInformation additionalInfo = additionalInfoGenerator.generateAdditionalInfo(request, bestProduct);

        // 5. 추천 상품 목록 조립(최적 상품 제외)
        List<RecommendedProductDto> recommendedProducts = loanRecommendationAssembler.assembleRecommendedProducts(filterResults, loanTerms, bestProduct);

        // 6. 사용자 정보 획득 및 입력 정보 기록
        User user = userService.getCurrentUser();
        UserInputInfo userInputInfo = userInputInfoService.recordUserInput(request, user);

        // 7. AI 보고서 생성
        String aiReport = aiReportService.generateAiReport(userInputInfo, bestProduct, additionalInfo, recommendedProducts);

        // 8. 결과 조립 및 저장
        LoanAdviceResult result = LoanAdviceResult.create(
            user, userInputInfo, bestProduct, additionalInfo, recommendedProducts, aiReport);
        loanAdviceResultRepository.save(result);

        return LoanAdviceResponse.of(result,
            userInputInfo.getId(),
            additionalInfo.getAvailableBanks(),
            additionalInfo.getProductFeatures());
    }

    public LoanAdviceResponse processSpecificLoanAdvice(Long userInputInfoId, String productCode) {
        // 기존 사용자 입력 정보 조회 후 요청 객체로 변환
        UserInputInfo userInputInfo = userInputInfoService.findById(userInputInfoId);
        LoanAdviceServiceRequest request = LoanAdviceServiceRequest.fromUserInputInfo(userInputInfo, productCode);
        return processLoanAdvice(request);
    }


    private static boolean hasEligibleProducts(List<FilterProductResultDto> filterResults) {
        return filterResults.stream().anyMatch(FilterProductResultDto::isEligible);
    }

}
