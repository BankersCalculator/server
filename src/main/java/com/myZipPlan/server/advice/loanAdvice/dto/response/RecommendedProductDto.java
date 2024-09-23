package com.myZipPlan.server.advice.loanAdvice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@ToString
public class RecommendedProductDto {

    private String loanProductName;
    private String loanProductCode;
    private BigDecimal possibleLoanLimit;
    private BigDecimal expectedLoanRate;
    private List<String> notEligibleReasons;

    public static RecommendedProductDto create(String loanProductName, String loanProductCode,
                                            BigDecimal possibleLoanLimit, BigDecimal expectedLoanRate,
                                               List<String> notEligibleReasons) {
        return RecommendedProductDto.builder()
            .loanProductName(loanProductName)
            .loanProductCode(loanProductCode)
            .possibleLoanLimit(possibleLoanLimit)
            .expectedLoanRate(expectedLoanRate)
            .notEligibleReasons(notEligibleReasons)
            .build();
    }
}