package com.bankersCalculator.server.advice.loanAdvice.domain;

import com.bankersCalculator.server.common.enums.Bank;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
public class LoanAdviceResult {
    // TODO: 검토 및 수정할 것. alternativeProducts 를 굳이 저장해야하는지 등.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                            // 대출상담결과 ID

    private String loanProductName;             // 대출 상품명
    private String loanProductCode;             // 대출 상품코드

    private double possibleLoanLimit;           // 가능한 대출 한도
    private double expectedLoanRate;            // 예상 대출 금리\

    private long totalRentalDeposit;            // 총 임대 보증금
    private long loanAmount;                    // 대출 금액
    private long ownFunds;                      // 소요 자기 자금

    private long monthlyInterestCost;           // 월 이자 비용
    private long monthlyRent;                   // 월 임대료

    private long opportunityCostOwnFunds;       // 기회 비용
    private double depositInterestRate;         // 예금 이자율

    private long guaranteeInsuranceFee;         // 보증 보험료
    private long stampDuty;                     // 인지세

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

}