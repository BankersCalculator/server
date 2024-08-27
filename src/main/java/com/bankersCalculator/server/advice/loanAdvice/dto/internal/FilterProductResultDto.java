package com.bankersCalculator.server.advice.loanAdvice.dto.internal;

import com.bankersCalculator.server.common.enums.loanAdvice.JeonseLoanProductType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class FilterProductResultDto {

    private JeonseLoanProductType productType;
    private boolean isEligible;
    private List<String> notEligibleReasons;
}
