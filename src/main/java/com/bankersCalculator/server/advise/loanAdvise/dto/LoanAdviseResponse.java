package com.bankersCalculator.server.advise.loanAdvise.dto;

import com.bankersCalculator.server.advise.loanAdvise.domain.LoanAdviseResult;
import com.bankersCalculator.server.advise.loanAdvise.domain.RecommendedProduct;
import com.bankersCalculator.server.common.enums.Bank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class LoanAdviseResponse {

    private long loanAdviseResultId;
    // 대출 상품 정보
    private String loanProductName;
    private String loanProductCode;
    private double possibleLoanLimit;
    private double expectedLoanRate;

    // 소요 자금 상세
    private long totalRentalDeposit;
    private long loanAmount;
    private long ownFunds;

    // 실 주거 비용
    private long monthlyInterestCost;
    private long monthlyRent;
    private long totalLivingCost;

    // 기회 비용
    private long opportunityCostOwnFunds;
    private double depositInterestRate;
    private long calculatedCost;

    // 부수 비용
    private long guaranteeInsuranceFee;
    private long stampDuty;

    // 추천 사유
    private String recommendationReason;

    // 기타 추천 상품 리스트
    private List<RecommendedProductDto> recommendedProducts;

    // 취급 가능 은행
    private List<Bank> availableBanks;

    // 전세 대출 가이드
    private String rentalLoanGuide;



    public static LoanAdviseResponse of(LoanAdviseResult result) {

        long totalLivingCost = result.getMonthlyRent() + result.getMonthlyRent();
        long calculatedCost = (long) (result.getOpportunityCostOwnFunds() * result.getDepositInterestRate());

        return LoanAdviseResponse.builder()
            .loanAdviseResultId(result.getId())
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
            .calculatedCost(calculatedCost)
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
                    .notEligibleReason(ap.getNotEligibleReason())
                    .build())
                .collect(Collectors.toList()))
            .availableBanks(result.getAvailableBanks())
            .rentalLoanGuide(result.getRentalLoanGuide())
            .build();
    }
}
