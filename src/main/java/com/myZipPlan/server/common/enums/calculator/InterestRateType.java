package com.myZipPlan.server.common.enums.calculator;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum InterestRateType {
    VARIABLE("변동형"),
    MIXED("혼합형"),
    PERIODIC("주기형");

    private final String description;

    public BigDecimal getMetroAreaInterestRateAddition() {
        if (this == VARIABLE) {
            return BigDecimal.valueOf(0.012);
        } else if (this == MIXED) {
            return BigDecimal.valueOf(0.0072);
        } else {
            return BigDecimal.valueOf(0.0036);
        }
    }

    public BigDecimal getNonMetroAreaInterestRateAddition() {
        if (this == VARIABLE) {
            return BigDecimal.valueOf(0.0075);
        } else if (this == MIXED) {
            return BigDecimal.valueOf(0.0045);
        } else {
            return BigDecimal.valueOf(0.0023);
        }
    }
}
