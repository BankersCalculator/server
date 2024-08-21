package com.bankersCalculator.server.calculator.dsrCalc.dto;

import com.bankersCalculator.server.calculator.dsrCalc.domain.DsrCalcResult;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DsrCalcResponse {

    private Integer annualIncome;
    private Integer totalLoanCount;
    private List<DsrCalcResult> dsrCalcResultList;
    private Double finalDsrRatio;
}
