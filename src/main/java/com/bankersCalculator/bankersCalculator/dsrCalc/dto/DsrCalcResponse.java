package com.bankersCalculator.bankersCalculator.dsrCalc.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DsrCalcResponse {

    private String name;
    private int firstValue;

    @Builder
    private DsrCalcResponse(String name, int firstValue) {
        this.name = name;
        this.firstValue = firstValue;
    }
}
