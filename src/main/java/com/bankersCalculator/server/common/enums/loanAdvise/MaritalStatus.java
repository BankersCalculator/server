package com.bankersCalculator.server.common.enums.loanAdvise;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MaritalStatus {

    SINGLE("미혼"),
    MARRIED("결혼 예정"),
    ENGAGED("기혼");

    private final String description;


}
