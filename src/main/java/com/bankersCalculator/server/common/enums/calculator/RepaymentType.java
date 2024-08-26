package com.bankersCalculator.server.common.enums.calculator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepaymentType {
    BULLET("일시상환"),
    AMORTIZING("원리금균등분할상환"),
    EQUAL_PRINCIPAL("원금균등분할상환");

    private final String description;
}
