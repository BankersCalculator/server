package com.bankersCalculator.server.advice.loanAdvice.entity;

import com.bankersCalculator.server.common.enums.Bank;
import com.bankersCalculator.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    @OrderBy("rank ASC")
    private List<RecommendedProduct> recommendedProducts; // 추천상품 리스트

    @ElementCollection(targetClass = Bank.class)
    @Enumerated(EnumType.STRING)
    private List<Bank> availableBanks;          // 이용 가능한 은행 목록

    @Column(length = 4000)
    private String rentalLoanGuide;             // 임대 대출 가이드

    public static LoanAdviceResult create(User user, String loanProductName,
                                          String loanProductCode, BigDecimal possibleLoanLimit,
                                          BigDecimal expectedLoanRate, BigDecimal totalRentalDeposit,
                                          BigDecimal loanAmount, BigDecimal ownFunds,
                                          BigDecimal monthlyInterestCost, BigDecimal monthlyRent,
                                          BigDecimal opportunityCostOwnFunds, BigDecimal depositInterestRate,
                                          BigDecimal guaranteeInsuranceFee, BigDecimal stampDuty,
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