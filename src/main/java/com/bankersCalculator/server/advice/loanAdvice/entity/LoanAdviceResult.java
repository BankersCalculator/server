package com.bankersCalculator.server.advice.loanAdvice.entity;

import com.bankersCalculator.server.advice.loanAdvice.dto.internal.AdditionalInformation;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.OptimalLoanProductResult;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.RecommendedProductDto;
import com.bankersCalculator.server.advice.userInputInfo.domain.UserInputInfo;
import com.bankersCalculator.server.common.enums.Bank;
import com.bankersCalculator.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class LoanAdviceResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                            // 대출상담결과 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_input_info_id")
    private UserInputInfo userInputInfo;

    private String loanProductName;             // 대출 상품명
    private String loanProductCode;             // 대출 상품코드

    private BigDecimal possibleLoanLimit;           // 가능한 대출 한도
    private BigDecimal expectedLoanRate;            // 예상 대출 금리

    private BigDecimal totalRentalDeposit;            // 총 임대 보증금
    private BigDecimal loanAmount;                    // 대출 금액
    private BigDecimal ownFunds;                      // 소요 자기 자금

    private BigDecimal monthlyInterestCost;           // 월 이자 비용
    private BigDecimal monthlyRent;                   // 월 임대료

    private BigDecimal opportunityCostOwnFunds;       // 기회 비용
    private BigDecimal depositInterestRate;         // 예금 이자율

    private BigDecimal guaranteeInsuranceFee;         // 보증 보험료
    private BigDecimal stampDuty;                     // 인지세

    @Column(length = 4000)
    private String recommendationReason;        // 추천 이유

    @OneToMany(mappedBy = "loanAdviceResult", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("possibleLoanLimit ASC")
    private List<RecommendedProduct> recommendedProducts; // 추천상품 리스트

    @ElementCollection(targetClass = Bank.class)
    @Enumerated(EnumType.STRING)
    private List<Bank> availableBanks;          // 이용 가능한 은행 목록

    @Column(length = 4000)
    private String rentalLoanGuide;             // 대출 가이드

    public static LoanAdviceResult create(User user, UserInputInfo userInputInfo,
                                          OptimalLoanProductResult optimalLoanProductResult,
                                          AdditionalInformation additionalInformation, List<RecommendedProduct> recommendedProducts,
                                          String aiReport) {

        BigDecimal totalRentalDeposit = optimalLoanProductResult.getPossibleLoanLimit()
            .add(additionalInformation.getOwnFunds());

        LoanAdviceResult result = LoanAdviceResult.builder()
            .user(user)
            .loanProductName(optimalLoanProductResult.getProductType().getProductName())
            .loanProductCode(optimalLoanProductResult.getProductType().getProductCode())
            .possibleLoanLimit(optimalLoanProductResult.getPossibleLoanLimit())
            .expectedLoanRate(optimalLoanProductResult.getExpectedLoanRate())
            .totalRentalDeposit(totalRentalDeposit)
            .loanAmount(optimalLoanProductResult.getPossibleLoanLimit())
            .ownFunds(additionalInformation.getOwnFunds())
            .monthlyInterestCost(additionalInformation.getMonthlyInterestCost())
            .monthlyRent(additionalInformation.getMonthlyRent())
            .opportunityCostOwnFunds(additionalInformation.getOpportunityCostOwnFunds())
            .depositInterestRate(additionalInformation.getDepositInterestRate())
            .guaranteeInsuranceFee(additionalInformation.getGuaranteeInsuranceFee())
            .stampDuty(additionalInformation.getStampDuty())
            .recommendationReason(aiReport)
            .recommendedProducts(recommendedProducts)
            .availableBanks(additionalInformation.getAvailableBanks())
            .rentalLoanGuide(additionalInformation.getRentalLoanGuide())
            .build();

        userInputInfo.setLoanAdviceResult(result);
        recommendedProducts.forEach(recommendedProduct -> recommendedProduct.setLoanAdviceResult(result));

        return result;
    }


    public LoanAdviceResponse toLoanAdviceResponse() {
        return LoanAdviceResponse.builder()
            .loanAdviceResultId(id)
            .loanProductName(loanProductName)
            .loanProductCode(loanProductCode)
            .possibleLoanLimit(possibleLoanLimit)
            .expectedLoanRate(expectedLoanRate)
            .totalRentalDeposit(totalRentalDeposit)
            .loanAmount(loanAmount)
            .ownFunds(ownFunds)
            .monthlyInterestCost(monthlyInterestCost)
            .monthlyRent(monthlyRent)
            .opportunityCostOwnFunds(opportunityCostOwnFunds)
            .depositInterestRate(depositInterestRate)
            .guaranteeInsuranceFee(guaranteeInsuranceFee)
            .stampDuty(stampDuty)
            .recommendationReason(recommendationReason)
            .recommendedProducts(convertToRecommendedProductDtoList(recommendedProducts))
            .availableBanks(availableBanks)
            .rentalLoanGuide(rentalLoanGuide)
            .build();
    }

    private List<RecommendedProductDto> convertToRecommendedProductDtoList(List<RecommendedProduct> recommendedProducts) {
        return recommendedProducts.stream()
            .map(this::convertToRecommendedProductDto)
            .collect(Collectors.toList());
    }

    private RecommendedProductDto convertToRecommendedProductDto(RecommendedProduct recommendedProduct) {
        return RecommendedProductDto.builder()
            .loanProductName(recommendedProduct.getLoanProductName())
            .loanProductCode(recommendedProduct.getLoanProductCode())
            .possibleLoanLimit(recommendedProduct.getPossibleLoanLimit())
            .expectedLoanRate(recommendedProduct.getExpectedLoanRate())
            .notEligibleReasons(Arrays.stream(recommendedProduct.getNotEligibleReasons()
                .split(Pattern.quote("|"))).toList())
            .build();
    }
}