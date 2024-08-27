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
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Slf4j
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
    private List<RecommendedProduct> recommendedProducts = new ArrayList<>();

    @ElementCollection(targetClass = Bank.class)
    @Enumerated(EnumType.STRING)
    private List<Bank> availableBanks = new ArrayList<>();  // 이용 가능한 은행 목록

    @Column(length = 4000)
    private String rentalLoanGuide;             // 대출 가이드

    public static LoanAdviceResult create(User user, UserInputInfo userInputInfo,
                                          OptimalLoanProductResult optimalLoanProductResult,
                                          AdditionalInformation additionalInformation,
                                          List<RecommendedProductDto> recommendedProductDtos,
                                          String aiReport) {

        BigDecimal totalRentalDeposit = optimalLoanProductResult.getPossibleLoanLimit()
            .add(additionalInformation.getOwnFunds());

        List<RecommendedProduct> recommendedProducts = recommendedProductDtos.stream()
            .map(RecommendedProduct::fromDto)
            .collect(Collectors.toList());


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
            .recommendedProducts(new ArrayList<>())
            .recommendationReason(aiReport)
            .availableBanks(additionalInformation.getAvailableBanks())
            .rentalLoanGuide(additionalInformation.getRentalLoanGuide())
            .build();

        result.updateRecommendedProducts(recommendedProducts);
        userInputInfo.setLoanAdviceResult(result);

        return result;
    }

    public void updateRecommendedProducts(List<RecommendedProduct> newRecommendedProducts) {
        if (this.recommendedProducts == null) {
            this.recommendedProducts = new ArrayList<>();
        }
        this.recommendedProducts.clear();
        for (RecommendedProduct product : newRecommendedProducts) {
            addRecommendedProduct(product);
        }
    }

    public void addRecommendedProduct(RecommendedProduct product) {
        this.recommendedProducts.add(product);
        product.setLoanAdviceResult(this);
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
            .recommendedProducts(recommendedProducts.stream()
                .map(RecommendedProduct::toDto)
                .collect(Collectors.toList()))
            .availableBanks(availableBanks)
            .rentalLoanGuide(rentalLoanGuide)
            .build();
    }

}