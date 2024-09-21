package com.myZipPlan.server.common.enums.calculator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InterestRateType {
    VARIABLE("변동형"),
    MIXED("혼합형"),
    PERIODIC("주기형");

    private final String description;
}
