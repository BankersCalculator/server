package com.bankersCalculator.server.advice.loanAdvice.service;
import com.bankersCalculator.server.advice.loanAdvice.dto.api.LoanAdviceResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.api.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.api.LoanAdviceSummaryResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.api.RecommendedProductDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.service.AdditionalInformation;
import com.bankersCalculator.server.advice.loanAdvice.dto.service.FilterProductResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.service.LoanLimitAndRateResult;
import com.bankersCalculator.server.advice.loanAdvice.dto.service.OptimalLoanProductResult;
import com.bankersCalculator.server.advice.loanAdvice.model.LoanProduct;
import com.bankersCalculator.server.common.enums.Bank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class LoanAdviceService {

    private final ProductFilter productFilter;
    private final LoanLimitAndRateCalculator loanLimitAndRateCalculator;
    private final ProductComparator productComparator;
    private final AdditionalInfoGenerator additionalInfoGenerator;
    private final AiReportGenerator aiReportGenerator;

    public LoanAdviceResponse generateLoanAdvice(LoanAdviceServiceRequest request) {


        // 대출상품 필터링
        List<FilterProductResultDto> filterResults = productFilter.filterProduct(request);

        // 대출한도 및 금리 계산
        List<LoanLimitAndRateResult> loanLimitAndRateResults = loanLimitAndRateCalculator.calculateLoanLimitAndRate(request, filterResults);

        // 대출상품 비교
        OptimalLoanProductResult optimalLoanProduct = productComparator.compareProducts(loanLimitAndRateResults);

        // 추가정보 생성
        AdditionalInformation additionalInformation = additionalInfoGenerator.generateAdditionalInfo();

        // 보고서 생성
        String aiReport = aiReportGenerator.generateAiReport();




        return LoanAdviceResponse.builder()
            .loanAdviceResultId(1L)
            .loanProductName("샘플 전세자금대출")
            .loanProductCode("SAMPLE001")
            .possibleLoanLimit(200000000L)
            .expectedLoanRate(3.5)
            .totalRentalDeposit(300000000L)
            .loanAmount(200000000L)
            .ownFunds(100000000L)
            .monthlyInterestCost(583333L)
            .monthlyRent(0L)
            .totalLivingCost(583333L)
            .opportunityCostOwnFunds(100000000L)
            .depositInterestRate(2.5)
            .guaranteeInsuranceFee(1000000L)
            .stampDuty(150000L)
            .recommendationReason("고객님의 소득과 신용도를 고려하여 가장 적합한 상품으로 선정되었습니다.")
            .recommendedProducts(Arrays.asList(
                RecommendedProductDto.builder()
                    .rank(2)
                    .loanProductName("신혼부부전용전세자금대출")
                    .loanProductCode("HF-001")
                    .possibleLoanLimit(180000000L)
                    .expectedLoanRate(3.7)
                    .notEligibleReasons(List.of())
                    .build(),
                RecommendedProductDto.builder()
                    .rank(3)
                    .loanProductName("서울시신혼부부임차보증금대출")
                    .loanProductCode("HF-002")
                    .possibleLoanLimit(220000000L)
                    .expectedLoanRate(3.8)
                    .notEligibleReasons(List.of("임차목적지가 서울시가 아닙니다."))
                    .build()
            ))
            .availableBanks(Arrays.asList(Bank.KOOMIN, Bank.SHINHAN, Bank.WOORI))
            .rentalLoanGuide("전세자금대출 이용 시 주의사항:\n1. 대출 기간 동안 이자를 꾸준히 납부해야 합니다.\n2. 전세 계약 만료 시 대출금 상환 계획을 미리 세워야 합니다.")
            .build();
    }



    public LoanAdviceResponse generateLoanAdviceOnSpecificLoan(Long loanAdviceResultId, String productCode) {
        return LoanAdviceResponse.builder()
            .loanAdviceResultId(1L)
            .loanProductName("샘플 전세자금대출")
            .loanProductCode("SAMPLE001")
            .possibleLoanLimit(200000000L)
            .expectedLoanRate(3.5)
            .totalRentalDeposit(300000000L)
            .loanAmount(200000000L)
            .ownFunds(100000000L)
            .monthlyInterestCost(583333L)
            .monthlyRent(0L)
            .totalLivingCost(583333L)
            .opportunityCostOwnFunds(100000000L)
            .depositInterestRate(2.5)
            .guaranteeInsuranceFee(1000000L)
            .stampDuty(150000L)
            .recommendationReason("고객님의 소득과 신용도를 고려하여 가장 적합한 상품으로 선정되었습니다.")
            .recommendedProducts(Arrays.asList(
                RecommendedProductDto.builder()
                    .rank(2)
                    .loanProductName("신혼부부전용전세자금대출")
                    .loanProductCode("HF-001")
                    .possibleLoanLimit(180000000L)
                    .expectedLoanRate(3.7)
                    .notEligibleReasons(List.of())
                    .build(),
                RecommendedProductDto.builder()
                    .rank(3)
                    .loanProductName("서울시신혼부부임차보증금대출")
                    .loanProductCode("HF-002")
                    .possibleLoanLimit(220000000L)
                    .expectedLoanRate(3.8)
                    .notEligibleReasons(List.of("임차목적지가 서울시가 아닙니다."))
                    .build()
            ))
            .availableBanks(Arrays.asList(Bank.KOOMIN, Bank.SHINHAN, Bank.WOORI))
            .rentalLoanGuide("전세자금대출 이용 시 주의사항:\n1. 대출 기간 동안 이자를 꾸준히 납부해야 합니다.\n2. 전세 계약 만료 시 대출금 상환 계획을 미리 세워야 합니다.")
            .build();
    }

    public List<LoanAdviceSummaryResponse> getRecentLoanAdvices() {
        return null;
    }

    public LoanAdviceResponse getSpecificLoanAdvice(Long loanAdviceResultId) {
        return null;
    }
}
