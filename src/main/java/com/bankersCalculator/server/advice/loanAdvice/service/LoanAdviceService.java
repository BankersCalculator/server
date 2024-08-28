package com.bankersCalculator.server.advice.loanAdvice.service;

import com.bankersCalculator.server.advice.loanAdvice.dto.internal.AdditionalInformation;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.BestLoanProductResult;
import com.bankersCalculator.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.RecommendedProductDto;
import com.bankersCalculator.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.bankersCalculator.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.bankersCalculator.server.advice.loanAdvice.service.component.*;
import com.bankersCalculator.server.advice.userInputInfo.domain.UserInputInfo;
import com.bankersCalculator.server.advice.userInputInfo.repository.UserInputInfoRepository;
import com.bankersCalculator.server.common.enums.loanAdvice.JeonseLoanProductType;
import com.bankersCalculator.server.oauth.userInfo.SecurityUtils;
import com.bankersCalculator.server.user.entity.User;
import com.bankersCalculator.server.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class LoanAdviceService {

    private final ProductFilter productFilter;
    private final LoanLimitAndRateCalculator loanLimitAndRateCalculator;
    private final ProductComparator productComparator;
    private final AdditionalInfoGenerator additionalInfoGenerator;
    private final AiReportGenerator aiReportGenerator;

    private final UserRepository userRepository;
    private final LoanAdviceResultRepository loanAdviceResultRepository;
    private final UserInputInfoRepository userInputInfoRepository;


    /**
     * 대출상품 추천을 위한 전체 프로세스를 수행한다.
     *
     * @param request
     * @return LoanAdviceResponse
     */
    public LoanAdviceResponse createLoanAdvice(LoanAdviceServiceRequest request) {

        // 대출상품 필터링
        List<FilterProductResultDto> filterResults = productFilter.filterProduct(request);
        // 필터링을 통과한 상품이 하나도 없는 경우
        if (!hasEligibleProducts(filterResults)) {
            return createEmptyLoanAdviceResponse(filterResults);
        }
        return createFullLoanAdviceResponse(request, filterResults);
    }

    public LoanAdviceResponse generateLoanAdviceOnSpecificLoan(Long loanAdviceResultId, String productCode) {
        return null;
    }

    private static boolean hasEligibleProducts(List<FilterProductResultDto> filterResults) {
        return filterResults.stream().anyMatch(FilterProductResultDto::isEligible);
    }

    private LoanAdviceResponse createEmptyLoanAdviceResponse(List<FilterProductResultDto> filterResults) {
        List<RecommendedProductDto> recommendedProductDtos = createIneligibleProductList(filterResults);
        return LoanAdviceResponse.ofEmpty(recommendedProductDtos);
    }

    private List<RecommendedProductDto> createIneligibleProductList(List<FilterProductResultDto> filterResults) {
        List<RecommendedProductDto> recommendedProductDtos = new ArrayList<>();
        for (FilterProductResultDto filterResult : filterResults) {
            JeonseLoanProductType productType = filterResult.getProductType();
            RecommendedProductDto recommendedProductDto = RecommendedProductDto.create(
                productType.getProductName(),
                productType.getProductCode(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                filterResult.getNotEligibleReasons()
            );
            recommendedProductDtos.add(recommendedProductDto);
        }
        return recommendedProductDtos;
    }

    private LoanAdviceResponse createFullLoanAdviceResponse(LoanAdviceServiceRequest request, List<FilterProductResultDto> filterResults) {
        LoanAdviceComponents components = prepareLoanAdviceComponents(request, filterResults);
        LoanAdviceResult result = assembleAndCreateResult(components);
        loanAdviceResultRepository.save(result);
        return result.toLoanAdviceResponse();
    }

    private LoanAdviceComponents prepareLoanAdviceComponents(LoanAdviceServiceRequest request, List<FilterProductResultDto> filterResults) {
        List<LoanLimitAndRateResultDto> loanTerms = calculateLoanTerms(request, filterResults);
        BestLoanProductResult bestProduct = findBestLoanProduct(request, loanTerms);
        AdditionalInformation additionalInfo = generateAdditionalInfo(request, bestProduct);
        String aiReport = generateAiReport();
        List<RecommendedProductDto> recommendedProducts = createRecommendedProductListExcludingBestLoan(filterResults, loanTerms, bestProduct);
        User user = fetchCurrentUser();
        UserInputInfo userInputInfo = createUserInputInfo(request, user);

        return new LoanAdviceComponents(bestProduct, additionalInfo, aiReport, recommendedProducts, user, userInputInfo);
    }

    private List<LoanLimitAndRateResultDto> calculateLoanTerms(LoanAdviceServiceRequest request, List<FilterProductResultDto> filterResults) {
        return loanLimitAndRateCalculator.calculateLoanLimitAndRate(request, filterResults);
    }

    private BestLoanProductResult findBestLoanProduct(LoanAdviceServiceRequest request, List<LoanLimitAndRateResultDto> loanTerms) {
        return productComparator.compareProducts(request.getRentalDeposit(), loanTerms);
    }

    private AdditionalInformation generateAdditionalInfo(LoanAdviceServiceRequest request, BestLoanProductResult bestProduct) {
        return additionalInfoGenerator.generateAdditionalInfo(request, bestProduct);
    }

    private String generateAiReport() {
        return aiReportGenerator.generateAiReport();
    }

    private List<RecommendedProductDto> createRecommendedProductListExcludingBestLoan(List<FilterProductResultDto> filterResults,
                                                                                      List<LoanLimitAndRateResultDto> loanLimitAndRateResultDto,
                                                                                      BestLoanProductResult optimalLoanProduct) {


        List<RecommendedProductDto> recommendedProductDtos = new ArrayList<>();

        // 간편한 조인을 위해 한도산출/금리 결과를 Map으로 변환
        Map<JeonseLoanProductType, LoanLimitAndRateResultDto> loanLimitAndRateMap = loanLimitAndRateResultDto.stream()
            .collect(Collectors.toMap(LoanLimitAndRateResultDto::getProductType, Function.identity()));

        for (FilterProductResultDto filterResult : filterResults) {
            JeonseLoanProductType productType = filterResult.getProductType();
            LoanLimitAndRateResultDto loanLimitAndRate = loanLimitAndRateMap.get(productType);

            // Best 상품을 제외한 나머지 추천상품 목록을 생성한다.
            if (productType == optimalLoanProduct.getProductType()) {
                continue;   // 최적상품은 제외
            }
            RecommendedProductDto recommendedProductDto = createRecommendedProduct(filterResult, productType, loanLimitAndRate);
            recommendedProductDtos.add(recommendedProductDto);
        }
        return recommendedProductDtos;
    }

    private static RecommendedProductDto createRecommendedProduct(FilterProductResultDto filterResult, JeonseLoanProductType productType, LoanLimitAndRateResultDto loanLimitAndRate) {
        BigDecimal loanLimit = loanLimitAndRate.getPossibleLoanLimit() == null ? BigDecimal.ZERO : loanLimitAndRate.getPossibleLoanLimit();
        BigDecimal expectedLoanRate = loanLimitAndRate.getExpectedLoanRate() == null ? BigDecimal.ZERO : loanLimitAndRate.getExpectedLoanRate();

        RecommendedProductDto recommendedProductDto = RecommendedProductDto.create(
            productType.getProductName(),
            productType.getProductCode(),
            loanLimit,
            expectedLoanRate,
            filterResult.getNotEligibleReasons()
        );
        return recommendedProductDto;
    }

    private User fetchCurrentUser() {
        String providerId = SecurityUtils.getProviderId();

        User user;
        if (providerId.startsWith("temp")) {
            user = userRepository.save(User.createTempUser(providerId));
        } else {
            user = userRepository.findByOauthProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다."));
        }
        return user;
    }

    private UserInputInfo createUserInputInfo(LoanAdviceServiceRequest request, User user) {
        UserInputInfo userInputInfo = UserInputInfo.create(user, request);
        userInputInfoRepository.save(userInputInfo);
        return userInputInfo;
    }

    private LoanAdviceResult assembleAndCreateResult(LoanAdviceComponents components) {
        return LoanAdviceResult.create(
            components.getUser(),
            components.getUserInputInfo(),
            components.getBestProduct(),
            components.getAdditionalInfo(),
            components.getRecommendedProducts(),
            components.getAiReport()
        );
    }

    @RequiredArgsConstructor
    @Getter
    private static class LoanAdviceComponents {
        private final BestLoanProductResult bestProduct;
        private final AdditionalInformation additionalInfo;
        private final String aiReport;
        private final List<RecommendedProductDto> recommendedProducts;
        private final User user;
        private final UserInputInfo userInputInfo;
    }
}
