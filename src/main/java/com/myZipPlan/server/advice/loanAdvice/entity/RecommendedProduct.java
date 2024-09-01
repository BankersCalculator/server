package com.myZipPlan.server.advice.loanAdvice.entity;

import com.myZipPlan.server.advice.loanAdvice.dto.response.RecommendedProductDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PROTECTED)
@Getter
@Entity
@ToString
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


    public RecommendedProductDto toDto() {
        return RecommendedProductDto.builder()
            .loanProductName(loanProductName)
            .loanProductCode(loanProductCode)
            .possibleLoanLimit(possibleLoanLimit)
            .expectedLoanRate(expectedLoanRate)
            .notEligibleReasons(Arrays.stream(notEligibleReasons.split(Pattern.quote("|"))).toList())
            .build();
    }

    public static RecommendedProduct fromDto(RecommendedProductDto dto) {
        return RecommendedProduct.builder()
            .loanProductName(dto.getLoanProductName())
            .loanProductCode(dto.getLoanProductCode())
            .possibleLoanLimit(dto.getPossibleLoanLimit())
            .expectedLoanRate(dto.getExpectedLoanRate())
            .notEligibleReasons(String.join("|", dto.getNotEligibleReasons()))
            .build();
    }

    public void setLoanAdviceResult(LoanAdviceResult loanAdviceResult) {
        this.loanAdviceResult = loanAdviceResult;
    }

}