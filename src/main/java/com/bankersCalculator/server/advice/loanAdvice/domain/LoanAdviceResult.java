package com.bankersCalculator.server.advice.loanAdvice.domain;

import com.bankersCalculator.server.common.enums.Bank;
import com.bankersCalculator.server.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    private String loanProductName;             // 대출 상품명
    private String loanProductCode;             // 대출 상품코드

    private Long possibleLoanLimit;           // 가능한 대출 한도
    private Double expectedLoanRate;            // 예상 대출 금리

    private Long totalRentalDeposit;            // 총 임대 보증금
    private Long loanAmount;                    // 대출 금액
    private Long ownFunds;                      // 소요 자기 자금

    private Long monthlyInterestCost;           // 월 이자 비용
    private Long monthlyRent;                   // 월 임대료

    private Long opportunityCostOwnFunds;       // 기회 비용
    private Double depositInterestRate;         // 예금 이자율

    private Long guaranteeInsuranceFee;         // 보증 보험료
    private Long stampDuty;                     // 인지세

    @Column(length = 4000)
    private String recommendationReason;        // 추천 이유

    @OneToMany(mappedBy = "loanAdviceResult", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("rank ASC")
    private List<RecommendedProduct> recommendedProducts; // 추천상품 리스트

    @ElementCollection(targetClass = Bank.class)
    @Enumerated(EnumType.STRING)
    private List<Bank> availableBanks;          // 이용 가능한 은행 목록

    @Column(length = 4000)
    private String rentalLoanGuide;             // 임대 대출 가이드

    public static LoanAdviceResult create(User user, String loanProductName,
                                          String loanProductCode, Long possibleLoanLimit,
                                          Double expectedLoanRate, Long totalRentalDeposit,
                                          Long loanAmount, Long ownFunds,
                                          Long monthlyInterestCost, Long monthlyRent,
                                          Long opportunityCostOwnFunds, Double depositInterestRate,
                                          Long guaranteeInsuranceFee, Long stampDuty,
                                          String recommendationReason, List<RecommendedProduct> recommendedProducts,
                                          List<Bank> availableBanks, String rentalLoanGuide) {
        return LoanAdviceResult.builder()
            .user(user)
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
            .recommendedProducts(recommendedProducts)
            .availableBanks(availableBanks)
            .rentalLoanGuide(rentalLoanGuide)
            .build();
    }
}