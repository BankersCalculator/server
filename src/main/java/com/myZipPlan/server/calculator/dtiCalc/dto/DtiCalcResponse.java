package com.myZipPlan.server.calculator.dtiCalc.dto;

import com.myZipPlan.server.calculator.dtiCalc.domain.DtiCalcResult;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class DtiCalcResponse {
    private Integer annualIncome;
    private Integer totalLoanCount;
    private List<DtiCalcResult> dtiCalcResults;
    private Double finalDtiRatio;
}