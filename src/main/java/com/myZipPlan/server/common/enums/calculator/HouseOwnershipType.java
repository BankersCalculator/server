package com.myZipPlan.server.common.enums.calculator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HouseOwnershipType {
    // 구입자금
    LIFETIME_FIRST("생애최초"),
    ORDINARY_DEMAND("서민실수요자"),
    NO_HOUSE("무주택"),
    SINGLE_HOUSE_DISPOSAL("1주택 처분 조건"),
    MORE_THAN_ONE_HOUSE("1주택 이상"),

    // 생활안정자금
    SINGLE_HOUSE("1주택"),
    MORE_THAN_TWO_HOUSE("2주택 이상"),
    ;

    private final String description;
}