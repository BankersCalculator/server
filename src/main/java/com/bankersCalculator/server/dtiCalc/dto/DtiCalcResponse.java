package com.bankersCalculator.server.dtiCalc.dto;
import java.util.List;

import com.bankersCalculator.server.dtiCalc.domain.DtiCalcResult;

import lombok.Builder;
import lombok.Getter;




@Getter
@Builder
public class DtiCalcResponse {
	private int annualIncome;
	private int totalLoanCount;
	private double finalDtiRatio;
	private List<DtiCalcResult> dtiCalcResultList;
	
	
	
}