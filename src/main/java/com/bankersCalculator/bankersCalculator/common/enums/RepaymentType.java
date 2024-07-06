package com.bankersCalculator.bankersCalculator.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepaymentType {
    Bullet("일시상환"),
    Amortizing("원리금균등분할상환"),
    EqualPrincipal("원금균등분할상환");

    private final String description;
}
