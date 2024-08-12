package com.bankersCalculator.server.calculator.dtiCalc.dto;

import com.bankersCalculator.server.calculator.dtiCalc.domain.DtiCalcResult;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class DtiCalcResponse {
    private int annualIncome;
    private int totalLoanCount;
    private List<DtiCalcResult> dtiCalcResultList;
    private double finalDtiRatio;
}