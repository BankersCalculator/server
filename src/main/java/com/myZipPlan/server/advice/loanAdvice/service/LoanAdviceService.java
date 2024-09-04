package com.myZipPlan.server.advice.loanAdvice.service;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.AdditionalInformation;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.BestLoanProductResult;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.myZipPlan.server.advice.loanAdvice.dto.response.RecommendedProductDto;
import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.myZipPlan.server.advice.loanAdvice.service.component.*;
import com.myZipPlan.server.advice.userInputInfo.domain.UserInputInfo;
import com.myZipPlan.server.advice.userInputInfo.repository.UserInputInfoRepository;
import com.myZipPlan.server.common.enums.Bank;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseLoanProductType;
import com.myZipPlan.server.common.exception.customException.AuthException;
import com.myZipPlan.server.oauth.userInfo.SecurityUtils;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
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

    public LoanAdviceResponse generateLoanAdviceOnSpecificLoan(Long userInputInfoId, String productCode) {
        UserInputInfo userInputInfo = userInputInfoRepository.findById(userInputInfoId)
            .orElseThrow(() -> new IllegalArgumentException("입력된 유저투입정보가 유효하지 않습니다."));

        LoanAdviceServiceRequest request = LoanAdviceServiceRequest.fromUserInputInfo(userInputInfo, productCode);

        List<FilterProductResultDto> filterResults = productFilter.filterSpecificProduct(request);
        // 필터링을 통과한 상품이 하나도 없는 경우
        if (!hasEligibleProducts(filterResults)) {
            return createEmptyLoanAdviceResponse(filterResults);
        }
        return createFullLoanAdviceResponse(request, filterResults);
    }

    private static boolean hasEligibleProducts(List<FilterProductResultDto> filterResults) {
        return filterResults.stream().anyMatch(FilterProductResultDto::isEligible);
    }

    private LoanAdviceResponse createEmptyLoanAdviceResponse(List<FilterProductResultDto> filterResults) {
        List<RecommendedProductDto> recommendedProductDtos = createIneligibleProductList(filterResults);
        return LoanAdviceResponse.ofEmpty(recommendedProductDtos);
    }

    // 필터링 통과 못한 상품들의 사유를 반환하기 위한 메서드
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

        Long userInputInfoId = components.userInputInfo.getId();
        List<Bank> availableBanks = components.additionalInfo.getAvailableBanks();
        loanAdviceResultRepository.save(result);
        return LoanAdviceResponse.of(result, userInputInfoId, availableBanks);
    }

    // 대출상품 추천을 위한 전체 프로세스를 수행한다.
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
        return productComparator.compareProducts(request.getRentalDeposit(), request.getSpecificRequestProductCode(), loanTerms);
    }

    private AdditionalInformation generateAdditionalInfo(LoanAdviceServiceRequest request, BestLoanProductResult bestProduct) {
        return additionalInfoGenerator.generateAdditionalInfo(request, bestProduct);
    }

    private String generateAiReport() {
        return aiReportGenerator.generateAiReport();
    }

    private List<RecommendedProductDto> createRecommendedProductListExcludingBestLoan(List<FilterProductResultDto> filterResults,
                                                                                      List<LoanLimitAndRateResultDto> loanLimitAndRateResultDto,
                                                                                      BestLoanProductResult bestLoanProduct) {


        List<RecommendedProductDto> recommendedProductDtos = new ArrayList<>();

        // 간편한 조인을 위해 한도산출/금리 결과를 Map으로 변환
        Map<JeonseLoanProductType, LoanLimitAndRateResultDto> loanLimitAndRateMap = loanLimitAndRateResultDto.stream()
            .collect(Collectors.toMap(LoanLimitAndRateResultDto::getProductType, Function.identity()));

        for (FilterProductResultDto filterResult : filterResults) {
            JeonseLoanProductType productType = filterResult.getProductType();
            LoanLimitAndRateResultDto loanLimitAndRate = loanLimitAndRateMap.get(productType);

            // Best 상품을 제외한 나머지 추천상품 목록을 생성한다.
            if (productType == bestLoanProduct.getProductType()) {
                continue;
            }
            RecommendedProductDto recommendedProductDto = createRecommendedProduct(filterResult, productType, loanLimitAndRate);
            recommendedProductDtos.add(recommendedProductDto);
        }
        return recommendedProductDtos;
    }

    private static RecommendedProductDto createRecommendedProduct(FilterProductResultDto filterResult, JeonseLoanProductType productType, LoanLimitAndRateResultDto loanLimitAndRate) {
        BigDecimal loanLimit = loanLimitAndRate.getPossibleLoanLimit() == null ? BigDecimal.ZERO : loanLimitAndRate.getPossibleLoanLimit();
        BigDecimal expectedLoanRate = loanLimitAndRate.getExpectedLoanRate() == null ? BigDecimal.ZERO : loanLimitAndRate.getExpectedLoanRate();

        return RecommendedProductDto.create(
            productType.getProductName(),
            productType.getProductCode(),
            loanLimit,
            expectedLoanRate,
            filterResult.getNotEligibleReasons()
        );
    }

    private User fetchCurrentUser() {
        String providerId = SecurityUtils.getProviderId();

        User user;
        if (providerId.startsWith("temp")) {
            user = userRepository.save(User.createTempUser(providerId));
        } else {
            user = userRepository.findByOauthProviderId(providerId)
                .orElseThrow(() -> new AuthException("사용자 정보가 없습니다."));
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
