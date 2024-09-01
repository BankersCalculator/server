package com.myZipPlan.server.calculator.dsrCalc.dto;

import com.myZipPlan.server.calculator.dsrCalc.domain.DsrCalcResult;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DsrCalcResponse {

    private Integer annualIncome;
    private Integer totalLoanCount;
    private List<DsrCalcResult> dsrCalcResults;
    private Double finalDsrRatio;
}
