package com.myZipPlan.server.common.enums.calculator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegionType {
    REGULATED_AREA("규제지역"),
    NON_REGULATED_CAPITAL_AREA("규제지역 외 수도권"),
    OTHER_AREAS("기타");

    private final String description;
}
