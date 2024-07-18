package com.bankersCalculator.bankersCalculator.dtiCalc.dto;
import java.math.BigDecimal;
import java.util.List;

import com.bankersCalculator.bankersCalculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcResponse;

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