package com.bankersCalculator.server.common.enums.loanAdvise;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RentalType {

    JEONSE("전세"),
    BANJEONSE("반전세"),
    WOLSE("월세");

    private final String description;


}