package com.myZipPlan.server.advice.loanAdvice.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SpecificLoanAdviceRequest {

    private Long loanAdviceResultId;
    private String productCode;


}