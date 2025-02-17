package com.myZipPlan.server.advice.loanAdvice.dto.response;

import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.common.enums.Bank;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@Builder
public class PreLoanTermsResponse {

    // 대출 상품 정보
    private String loanProductName;
    private String loanProductCode;
    private BigDecimal possibleLoanLimit;
    private BigDecimal expectedLoanRate;

}
