package com.bankersCalculator.bankersCalculator.common.enums.ltv;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoanPurpose {
    HOME_PURCHASE("주택구입자금"),
    LIVING_STABILITY("생활안정자금");

    private final String description;

}
