package com.bankersCalculator.bankersCalculator.dsrCalc.dto;

import com.bankersCalculator.bankersCalculator.dsrCalc.domain.DsrCalcResult;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DsrCalcResponse {

    private int annualIncome;
    private int totalLoanCount;
    private List<DsrCalcResult> dsrCalcResultList;
    private double finalDsrRatio;
}
