package com.bankersCalculator.bankersCalculator.dsrCalc.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DsrCalcRequest {

    private String testName;
    private Integer firstValue;

    @Builder
    public DsrCalcRequest(String testName, int firstValue) {
        this.testName = testName;
        this.firstValue = firstValue;
    }
}
