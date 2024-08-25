package com.bankersCalculator.server.advice.loanAdvice.dto.service;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FilterProductResultDto {

    private String LoanProductCode;
    private boolean isEligible;
    private List<String> notEligibleReasons;
}
