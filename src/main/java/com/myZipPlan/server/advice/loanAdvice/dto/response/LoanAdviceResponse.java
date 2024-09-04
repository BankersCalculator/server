package com.myZipPlan.server.advice.loanAdvice.dto.response;

import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.common.enums.Bank;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@Builder
public class LoanAdviceResponse {

    private Long loanAdviceResultId;
    private Long userInputInfoId;

    private Boolean hasEligibleProduct;
    // 대출 상품 정보
    private String loanProductName;
    private String loanProductCode;
    private BigDecimal possibleLoanLimit;
    private BigDecimal expectedLoanRate;

    // 소요 자금 상세
    private BigDecimal totalRentalDeposit;
    private BigDecimal loanAmount;    // 대출금
    private BigDecimal ownFunds;  // 자기자금

    // 실 주거 비용
    private BigDecimal monthlyInterestCost;  // 월 이자 비용
    private BigDecimal monthlyRent;  // 월세
    private BigDecimal totalLivingCost;  // 총 주거 비용

    // 기회 비용
    private BigDecimal opportunityCostOwnFunds; // 기회비용
    private BigDecimal depositInterestRate; // 예금 이자율

    // 부수 비용
    private BigDecimal guaranteeInsuranceFee; // 보증보험료
    private BigDecimal stampDuty; // 인지세

    // 추천 사유
    private String recommendationReason; // AI 추천 사유

    // 기타 추천 상품 리스트
    private List<RecommendedProductDto> recommendedProducts;

    // 취급 가능 은행
    private List<Bank> availableBanks;

    // 전세 대출 가이드
    private String rentalLoanGuide;


    // Eilgible 상품이 없는 경우
    public static LoanAdviceResponse ofEmpty(List<RecommendedProductDto> recommendedProductDto) {
        return LoanAdviceResponse.builder()
            .hasEligibleProduct(false)
            .recommendedProducts(recommendedProductDto)
            .build();
    }



    public static LoanAdviceResponse of(LoanAdviceResult result, Long userInputInfoId, List<Bank> availableBanks) {

        BigDecimal totalLivingCost = result.getMonthlyRent().add(result.getMonthlyRent());

        return LoanAdviceResponse.builder()
            .loanAdviceResultId(result.getId())
            .userInputInfoId(userInputInfoId)
            .hasEligibleProduct(true)
            .loanProductName(result.getLoanProductName())
            .loanProductCode(result.getLoanProductCode())
            .possibleLoanLimit(result.getPossibleLoanLimit())
            .expectedLoanRate(result.getExpectedLoanRate())
            .totalRentalDeposit(result.getTotalRentalDeposit())
            .loanAmount(result.getLoanAmount())
            .ownFunds(result.getOwnFunds())
            .monthlyInterestCost(result.getMonthlyInterestCost())
            .monthlyRent(result.getMonthlyRent())
            .totalLivingCost(totalLivingCost)
            .opportunityCostOwnFunds(result.getOpportunityCostOwnFunds())
            .depositInterestRate(result.getDepositInterestRate())
            .guaranteeInsuranceFee(result.getGuaranteeInsuranceFee())
            .stampDuty(result.getStampDuty())
            .recommendationReason(result.getRecommendationReason())
            .recommendedProducts(result.getRecommendedProducts().stream()
                .map(ap -> RecommendedProductDto.builder()
                    .loanProductName(ap.getLoanProductName())
                    .loanProductCode(ap.getLoanProductCode())
                    .possibleLoanLimit(ap.getPossibleLoanLimit())
                    .expectedLoanRate(ap.getExpectedLoanRate())
                    .notEligibleReasons(Arrays.stream(ap.getNotEligibleReasons().split(Pattern.quote("|"))).toList())
                    .build())
                .collect(Collectors.toList()))
            .availableBanks(availableBanks)
            .rentalLoanGuide(result.getRentalLoanGuide())
            .build();
    }
}
