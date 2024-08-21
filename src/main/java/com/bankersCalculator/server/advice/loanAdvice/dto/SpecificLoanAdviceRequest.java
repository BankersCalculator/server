package com.bankersCalculator.server.advice.loanAdvice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SpecificLoanAdviceRequest {

    private Long loanAdviceResultId;
    private String productCode;


}