package com.bankersCalculator.bankersCalculator.common.enums.ltv;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RealEstateManagementAreaType {
    OVERHEATED_SPECULATION_AREA("투기과열지구"),
    SPECULATION_AREA("투기지역"),
    ADJUSTMENT_TARGET_AREA("조정대상지역"),
    NORMAL_AREA("일반지역");

    private final String description;
}
