package com.bankersCalculator.server.advice.loanAdvice.dto.response;

import com.bankersCalculator.server.advice.loanAdvice.entity.RecommendedProduct;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
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