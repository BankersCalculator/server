package com.myZipPlan.server.common.enums.loanAdvice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JeonseHouseOwnershipType {

    NO_HOUSE("무주택"),
    SINGLE_HOUSE("1주택"),
    MULTI_HOUSE("다주택"),
    ;

    private final String description;
}