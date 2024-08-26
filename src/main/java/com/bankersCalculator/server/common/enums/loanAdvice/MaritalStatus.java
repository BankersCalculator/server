package com.bankersCalculator.server.common.enums.loanAdvice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MaritalStatus {

    SINGLE("미혼"),
    ENGAGED("결혼 예정"),
    NEWLY_MARRIED("신혼"),
    MARRIED("기혼");

    private final String description;
}
