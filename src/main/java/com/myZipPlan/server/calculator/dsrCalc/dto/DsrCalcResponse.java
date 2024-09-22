package com.myZipPlan.server.calculator.dsrCalc.dto;

import com.myZipPlan.server.calculator.dsrCalc.domain.DsrCalcResult;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class DsrCalcResponse {

    private BigDecimal annualIncome;
    private BigDecimal totalLoanCount;
    private List<DsrCalcResult> dsrCalcResults;
    private BigDecimal finalDsrRatio;
}
