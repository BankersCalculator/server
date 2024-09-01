package com.myZipPlan.server.common.enums.loanAdvice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AreaSize {

    UNDER_85_SQM("85제곱이하"),
    OVER_85_SQM("85제곱초과");

    private final String description;


}
