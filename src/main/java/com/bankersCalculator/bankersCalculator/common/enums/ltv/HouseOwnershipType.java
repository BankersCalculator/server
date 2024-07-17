package com.bankersCalculator.bankersCalculator.common.enums.ltv;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HouseOwnershipType {
    LIFETIME_FIRST("생애최초"),
    ORDINARY_DEMAND("서민실수요자"),
    NO_HOUSE("무주택"),
    SINGLE_HOUSE("1주택"),
    MULTI_HOUSE("다주택");

    private final String description;
}