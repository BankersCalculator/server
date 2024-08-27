package com.bankersCalculator.server.advice.loanAdvice.service;

import com.bankersCalculator.server.advice.loanAdvice.dto.internal.AdditionalInformation;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.OptimalLoanProductResult;
import com.bankersCalculator.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
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
import jakarta.persistence.EntityManager;
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
    private final EntityManager entityManager;

    private final UserRepository userRepository;
    private final LoanAdviceResultRepository loanAdviceResultRepository;
    private final UserInputInfoRepository userInputInfoRepository;


    /**
     * 대출상품 추천을 위한 전체 프로세스를 수행한다.
     *
     * @param request
     * @return LoanAdviceResponse
     */
    public LoanAdviceResponse generateLoanAdvice(LoanAdviceServiceRequest request) {

        // 대출상품 필터링
        List<FilterProductResultDto> filterResults = productFilter.filterProduct(request);
        // 대출한도 및 금리 계산
        List<LoanLimitAndRateResultDto> loanLimitAndRateResultDto = loanLimitAndRateCalculator.calculateLoanLimitAndRate(request, filterResults);
        // 대출상품 비교
        OptimalLoanProductResult optimalLoanProduct = productComparator.compareProducts(request.getRentalDeposit(), loanLimitAndRateResultDto);
        // 추가정보 생성
        AdditionalInformation additionalInformation = additionalInfoGenerator.generateAdditionalInfo(request, optimalLoanProduct);
        // 보고서 생성
        String aiReport = aiReportGenerator.generateAiReport();
        // 최적상품 외 추천상품목록 생성
        List<RecommendedProductDto> recommendedProductDtos = generateRecommendProductDtos(filterResults, loanLimitAndRateResultDto, optimalLoanProduct);

        User user = getCurrentUser();
        UserInputInfo userInputInfo = getUserInputInfo(request, user);
        LoanAdviceResult loanAdviceResult = getLoanAdviceResult(optimalLoanProduct, additionalInformation, aiReport, recommendedProductDtos, user, userInputInfo);
        return loanAdviceResult.toLoanAdviceResponse();
    }


    public LoanAdviceResponse generateLoanAdviceOnSpecificLoan(Long loanAdviceResultId, String productCode) {
        return null;
    }

    public List<LoanAdviceSummaryResponse> getRecentLoanAdvices() {
        return null;
    }

    public LoanAdviceResponse getSpecificLoanAdvice(Long loanAdviceResultId) {
        return null;
    }

    private User getCurrentUser() {
        String providerId = SecurityUtils.getProviderId();
        User user = userRepository.findByOauthProviderId(providerId)
            .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다."));
        return user;
    }

    private UserInputInfo getUserInputInfo(LoanAdviceServiceRequest request, User user) {
        UserInputInfo userInputInfo = UserInputInfo.create(user, request);
        userInputInfoRepository.save(userInputInfo);
        return userInputInfo;
    }

    private LoanAdviceResult getLoanAdviceResult(OptimalLoanProductResult optimalLoanProduct,
                                                 AdditionalInformation additionalInformation,
                                                 String aiReport,
                                                 List<RecommendedProductDto> recommendedProductDtos,
                                                 User user,
                                                 UserInputInfo userInputInfo) {
        LoanAdviceResult loanAdviceResult = LoanAdviceResult.create(user, userInputInfo, optimalLoanProduct,
            additionalInformation, recommendedProductDtos, aiReport);
//        loanAdviceResultRepository.save(loanAdviceResult); // TODO: UnsupportedOperationException 발생. 원인 파악 필요. 우선 em.persist로 대체
        entityManager.persist(loanAdviceResult);
        return loanAdviceResult;
    }

    /**
     * 보고서 생성 중 산출된 값을 종합해서
     * 2등 이하의 추천상품 목록을 생성한다.
     *
     * @param filterResults
     * @param loanLimitAndRateResultDto
     * @param optimalLoanProduct
     * @return List<RecommendedProduct>
     */
    private List<RecommendedProductDto> generateRecommendProductDtos(List<FilterProductResultDto> filterResults,
                                                                     List<LoanLimitAndRateResultDto> loanLimitAndRateResultDto,
                                                                     OptimalLoanProductResult optimalLoanProduct) {

        List<RecommendedProductDto> recommendedProductDtos = new ArrayList<>();

        // 간편한 조인을 위해 한도산출/금리 결과를 Map으로 변환
        Map<JeonseLoanProductType, LoanLimitAndRateResultDto> loanLimitAndRateMap = loanLimitAndRateResultDto.stream()
            .collect(Collectors.toMap(LoanLimitAndRateResultDto::getProductType, Function.identity()));

        for (FilterProductResultDto filterResult : filterResults) {
            JeonseLoanProductType productType = filterResult.getProductType();
            LoanLimitAndRateResultDto loanLimitAndRate = loanLimitAndRateMap.get(productType);

            if (productType == optimalLoanProduct.getProductType()) {
                continue;   // 최적상품은 제외
            }
            RecommendedProductDto recommendedProductDto = getRecommendedProductDto(filterResult, productType, loanLimitAndRate);
            recommendedProductDtos.add(recommendedProductDto);
        }
        return recommendedProductDtos;
    }

    private static RecommendedProductDto getRecommendedProductDto(FilterProductResultDto filterResult, JeonseLoanProductType productType, LoanLimitAndRateResultDto loanLimitAndRate) {
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


}
