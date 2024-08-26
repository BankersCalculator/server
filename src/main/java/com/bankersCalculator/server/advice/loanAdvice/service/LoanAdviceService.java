package com.bankersCalculator.server.advice.loanAdvice.service;

import com.bankersCalculator.server.advice.loanAdvice.dto.internal.AdditionalInformation;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.OptimalLoanProductResult;
import com.bankersCalculator.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.bankersCalculator.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.bankersCalculator.server.advice.loanAdvice.entity.RecommendedProduct;
import com.bankersCalculator.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.bankersCalculator.server.advice.loanAdvice.service.component.*;
import com.bankersCalculator.server.advice.userInputInfo.domain.UserInputInfo;
import com.bankersCalculator.server.advice.userInputInfo.repository.UserInputInfoRepository;
import com.bankersCalculator.server.common.enums.loanAdvice.JeonseLoanProductType;
import com.bankersCalculator.server.oauth.userInfo.SecurityUtils;
import com.bankersCalculator.server.user.entity.User;
import com.bankersCalculator.server.user.repository.UserRepository;
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


    @Transactional
    public LoanAdviceResponse generateLoanAdvice(LoanAdviceServiceRequest request) {

        String providerId = SecurityUtils.getProviderId();
        User user = userRepository.findByOauthProviderId(providerId)
            .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다."));

        // 대출상품 필터링
        List<FilterProductResultDto> filterResults = productFilter.filterProduct(request);

        // 대출한도 및 금리 계산
        List<LoanLimitAndRateResultDto> loanLimitAndRateResultDto = loanLimitAndRateCalculator.calculateLoanLimitAndRate(request, filterResults);

        // 대출상품 비교
        BigDecimal rentalDeposit = request.getRentalDeposit();
        OptimalLoanProductResult optimalLoanProduct = productComparator.compareProducts(rentalDeposit, loanLimitAndRateResultDto);

        // 추가정보 생성
        AdditionalInformation additionalInformation = additionalInfoGenerator.generateAdditionalInfo(request, optimalLoanProduct);

        // 보고서 생성
        String aiReport = aiReportGenerator.generateAiReport();

        List<RecommendedProduct> recommendedProducts = generateRecommendProducts(filterResults, loanLimitAndRateResultDto, optimalLoanProduct);

        UserInputInfo userInputInfo = UserInputInfo.create(user, request);
        LoanAdviceResult loanAdviceResult = LoanAdviceResult.create(user, userInputInfo, optimalLoanProduct, additionalInformation, recommendedProducts, aiReport);

        userInputInfoRepository.save(userInputInfo);
        loanAdviceResultRepository.save(loanAdviceResult);

        return loanAdviceResult.toLoanAdviceResponse();
    }

    /**
     * 추천상품 생성
     *
     * 보고서 생성 중 산출된 값을 종합해서
     * 2등 이하의 추천상품 목록을 생성한다.
     *
     * @param filterResults
     * @param loanLimitAndRateResultDto
     * @param optimalLoanProduct
     * @return List<RecommendedProduct>
     */
    private List<RecommendedProduct> generateRecommendProducts(List<FilterProductResultDto> filterResults,
                                                               List<LoanLimitAndRateResultDto> loanLimitAndRateResultDto,
                                                               OptimalLoanProductResult optimalLoanProduct) {
        List<RecommendedProduct> recommendedProducts = new ArrayList<>();

        Map<JeonseLoanProductType, LoanLimitAndRateResultDto> loanLimitAndRateMap = loanLimitAndRateResultDto.stream()
            .collect(Collectors.toMap(LoanLimitAndRateResultDto::getProductType, Function.identity()));

        for (FilterProductResultDto filterResult : filterResults) {
            JeonseLoanProductType productType = filterResult.getProductType();
            LoanLimitAndRateResultDto loanLimitAndRate = loanLimitAndRateMap.get(productType);

            if (productType == optimalLoanProduct.getProductType()) {
                continue;
            }

            if (loanLimitAndRate != null) {
                String notEligibleReasons = String.join("|", filterResult.getNotEligibleReasons());
                RecommendedProduct recommendedProduct = RecommendedProduct.create(
                    null, productType.getProductName(),
                    productType.getProductCode(),
                    loanLimitAndRate.getPossibleLoanLimit(), loanLimitAndRate.getExpectedLoanRate(),
                    notEligibleReasons);

                recommendedProducts.add(recommendedProduct);
            }
        }
        return recommendedProducts;
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
}
