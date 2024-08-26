package com.bankersCalculator.server.advice.loanAdvice.entity;

import com.bankersCalculator.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RecommendedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_advice_result_id")
    private LoanAdviceResult loanAdviceResult;

    private String loanProductName; // 대출 상품명
    private String loanProductCode; // 대출 상품코드
    private BigDecimal possibleLoanLimit; // 가능한 대출 한도
    private BigDecimal expectedLoanRate; // 예상 대출 금리
    private String notEligibleReasons; // 부적격 사유. 각 사유를 |(파이프라인)으로 구분지어서 적재

    public static RecommendedProduct create(LoanAdviceResult loanAdviceResult, String loanProductName,
                                            String loanProductCode, BigDecimal possibleLoanLimit,
                                            BigDecimal expectedLoanRate, String notEligibleReasons) {
        return RecommendedProduct.builder()
                .loanAdviceResult(loanAdviceResult)
                .loanProductName(loanProductName)
                .loanProductCode(loanProductCode)
                .possibleLoanLimit(possibleLoanLimit)
                .expectedLoanRate(expectedLoanRate)
                .notEligibleReasons(notEligibleReasons)
                .build();
    }

    public void setLoanAdviceResult(LoanAdviceResult result) {
        this.loanAdviceResult = result;
    }
}