package com.bankersCalculator.server.advice.loanAdvice.service;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.RecommendedProductDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.AdditionalInformation;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.OptimalLoanProductResult;
import com.bankersCalculator.server.advice.loanAdvice.service.component.*;
import com.bankersCalculator.server.common.enums.Bank;
import com.bankersCalculator.server.oauth.userInfo.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        List<LoanLimitAndRateResultDto> loanLimitAndRateResultDto = loanLimitAndRateCalculator.calculateLoanLimitAndRate(request, filterResults);

        // 대출상품 비교
        BigDecimal rentalDeposit = request.getRentalDeposit();
        OptimalLoanProductResult optimalLoanProduct = productComparator.compareProducts(rentalDeposit, loanLimitAndRateResultDto);

        // 추가정보 생성
        AdditionalInformation additionalInformation = additionalInfoGenerator.generateAdditionalInfo();

        // 보고서 생성
        String aiReport = aiReportGenerator.generateAiReport();


        // UserInputInfo 저장
        // LoanAdviceResult 저장

        return LoanAdviceResponse.builder()
            .loanAdviceResultId(1L)
            .loanProductName("샘플 전세자금대출")
            .loanProductCode("SAMPLE001")
            .possibleLoanLimit(BigDecimal.valueOf(200000000))
            .expectedLoanRate(BigDecimal.valueOf(3.5))
            .totalRentalDeposit(BigDecimal.valueOf(300000000))
            .loanAmount(BigDecimal.valueOf(200000000))
            .ownFunds(BigDecimal.valueOf(100000000))
            .monthlyInterestCost(BigDecimal.valueOf(583333))
            .monthlyRent(BigDecimal.valueOf(0L))
            .totalLivingCost(BigDecimal.valueOf(583333L))
            .opportunityCostOwnFunds(BigDecimal.valueOf(100000000))
            .depositInterestRate(BigDecimal.valueOf(2.5))
            .guaranteeInsuranceFee(BigDecimal.valueOf(1000000))
            .stampDuty(BigDecimal.valueOf(150000))
            .recommendationReason("고객님의 소득과 신용도를 고려하여 가장 적합한 상품으로 선정되었습니다.")
            .recommendedProducts(Arrays.asList(
                RecommendedProductDto.builder()
                    .loanProductName("신혼부부전용전세자금대출")
                    .loanProductCode("HF-001")
                    .possibleLoanLimit(BigDecimal.valueOf(180000000))
                    .expectedLoanRate(BigDecimal.valueOf(3.7))
                    .notEligibleReasons(List.of())
                    .build(),
                RecommendedProductDto.builder()
                    .loanProductName("서울시신혼부부임차보증금대출")
                    .loanProductCode("HF-002")
                    .possibleLoanLimit(BigDecimal.valueOf(220000000))
                    .expectedLoanRate(BigDecimal.valueOf(3.8))
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
            .possibleLoanLimit(BigDecimal.valueOf(200000000))
            .expectedLoanRate(BigDecimal.valueOf(3.5))
            .totalRentalDeposit(BigDecimal.valueOf(300000000))
            .loanAmount(BigDecimal.valueOf(200000000))
            .ownFunds(BigDecimal.valueOf(100000000))
            .monthlyInterestCost(BigDecimal.valueOf(583333))
            .monthlyRent(BigDecimal.valueOf(0L))
            .totalLivingCost(BigDecimal.valueOf(583333L))
            .opportunityCostOwnFunds(BigDecimal.valueOf(100000000))
            .depositInterestRate(BigDecimal.valueOf(2.5))
            .guaranteeInsuranceFee(BigDecimal.valueOf(1000000))
            .stampDuty(BigDecimal.valueOf(150000))
            .recommendationReason("고객님의 소득과 신용도를 고려하여 가장 적합한 상품으로 선정되었습니다.")
            .recommendedProducts(Arrays.asList(
                RecommendedProductDto.builder()
                    .loanProductName("신혼부부전용전세자금대출")
                    .loanProductCode("HF-001")
                    .possibleLoanLimit(BigDecimal.valueOf(180000000))
                    .expectedLoanRate(BigDecimal.valueOf(3.7))
                    .notEligibleReasons(List.of())
                    .build(),
                RecommendedProductDto.builder()
                    .loanProductName("서울시신혼부부임차보증금대출")
                    .loanProductCode("HF-002")
                    .possibleLoanLimit(BigDecimal.valueOf(220000000))
                    .expectedLoanRate(BigDecimal.valueOf(3.8))
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
