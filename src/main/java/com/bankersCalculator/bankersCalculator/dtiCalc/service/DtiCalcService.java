package com.bankersCalculator.bankersCalculator.dtiCalc.service;


import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcServiceRequest;

import lombok.RequiredArgsConstructor;

import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcResponse;
import com.bankersCalculator.bankersCalculator.dtiCalc.calculator.DtiCalculator;
import com.bankersCalculator.bankersCalculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcServiceRequest;
import com.bankersCalculator.bankersCalculator.dtiCalc.calculator.DtiCalculator;
import com.bankersCalculator.bankersCalculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcRequest;
import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcResponse;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DtiCalcService {
	

	/* 
	 * 프로그램 설명 :DTI 계산기 Service 
	 * 개발자 : 제갈명필
	 * 수정  : 2024-07-13, 리펙토링.  
	 */
	
	private DtiCalculator dtiCalculator;
    public DtiCalcResponse dticalculate(DtiCalcServiceRequest request) {
    	double totalDtiAmount = 0;
    	int annualIncome = request.getAnnualIncome();
    	int totalLoanCount = 0;
    	   
    	List<DtiCalcResult> dtiCalcResultList = new ArrayList<>();
    	for (DtiCalcServiceRequest.LoanStatus loanStatus : request.getLoanStatusList()) { 
    		DtiCalcResult dtiCalcResult = dtiCalculator.calculateDti(loanStatus);
    		
    		dtiCalcResult.setSerial(++totalLoanCount);
    		totalDtiAmount += dtiCalcResult.getAnnualPrincipalRepayment();            
            totalDtiAmount += dtiCalcResult.getAnnualInterestRepayment();

            dtiCalcResultList.add(dtiCalcResult);
    	}
    	
    	double totalDtiRatio = (totalDtiAmount / annualIncome);

        return DtiCalcResponse.builder()
            .annualIncome(annualIncome)
            .totalLoanCount(totalLoanCount)
            .dtiCalcResultList(dtiCalcResultList)
            .finalDtiRatio(totalDtiRatio)
            .build();
    }   	 
}
