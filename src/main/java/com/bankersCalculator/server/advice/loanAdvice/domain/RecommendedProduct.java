package com.bankersCalculator.server.advice.loanAdvice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommended_product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_advice_result_id")
    private LoanAdviceResult loanAdviceResult;

    private int rank; // 추천 순위

    private String loanProductName; // 대출 상품명
    private String loanProductCode; // 대출 상품코드
    private Long possibleLoanLimit; // 가능한 대출 한도
    private Double expectedLoanRate; // 예상 대출 금리
    private String notEligibleReasons; // 부적격 사유 (해당되는 경우) TODO: LIST로 저장하려는데.. 어떻게 해야할지 고민해보기 우선 Docs에는 parse로 분할함

}