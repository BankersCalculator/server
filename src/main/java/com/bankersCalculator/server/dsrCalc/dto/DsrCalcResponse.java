package com.bankersCalculator.server.dsrCalc.dto;

import com.bankersCalculator.server.dsrCalc.domain.DsrCalcResult;
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
