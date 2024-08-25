package com.bankersCalculator.server.advice.loanAdvice.dto.api;

import com.bankersCalculator.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.bankersCalculator.server.common.enums.Bank;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@Builder
public class LoanAdviceResponse {

    private Long loanAdviceResultId;
    // 대출 상품 정보
    private String loanProductName;
    private String loanProductCode;
    private Long possibleLoanLimit;
    private Double expectedLoanRate;

    // 소요 자금 상세
    private Long totalRentalDeposit;
    private Long loanAmount;    // 대출금
    private Long ownFunds;  // 자기자금

    // 실 주거 비용
    private Long monthlyInterestCost;  // 월 이자 비용
    private Long monthlyRent;  // 월세
    private Long totalLivingCost;  // 총 주거 비용

    // 기회 비용
    private Long opportunityCostOwnFunds; // 기회비용
    private Double depositInterestRate; // 예금 이자율

    // 부수 비용
    private Long guaranteeInsuranceFee; // 보증보험료
    private Long stampDuty; // 인지세

    // 추천 사유
    private String recommendationReason; // AI 추천 사유

    // 기타 추천 상품 리스트
    private List<RecommendedProductDto> recommendedProducts;

    // 취급 가능 은행
    private List<Bank> availableBanks;

    // 전세 대출 가이드
    private String rentalLoanGuide;



    public static LoanAdviceResponse of(LoanAdviceResult result) {

        long totalLivingCost = result.getMonthlyRent() + result.getMonthlyRent();
        long calculatedCost = (long) (result.getOpportunityCostOwnFunds() * result.getDepositInterestRate());

        return LoanAdviceResponse.builder()
            .loanAdviceResultId(result.getId())
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
                    .rank(ap.getRank())
                    .loanProductName(ap.getLoanProductName())
                    .loanProductCode(ap.getLoanProductCode())
                    .possibleLoanLimit(ap.getPossibleLoanLimit())
                    .expectedLoanRate(ap.getExpectedLoanRate())
                    .notEligibleReasons(Arrays.stream(ap.getNotEligibleReasons().split(Pattern.quote("|"))).toList())
                    .build())
                .collect(Collectors.toList()))
            .availableBanks(result.getAvailableBanks())
            .rentalLoanGuide(result.getRentalLoanGuide())
            .build();
    }
}
