package com.bankersCalculator.server.advice.loanAdvice.dto;

import com.bankersCalculator.server.common.enums.loanAdvise.UserType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SpecificLoanAdviceRequest {

    private final Long loanAdviceResultId;
    private final String productCode;


}