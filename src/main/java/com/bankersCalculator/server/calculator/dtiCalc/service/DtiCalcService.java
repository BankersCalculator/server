package com.bankersCalculator.server.calculator.dtiCalc.service;


import com.bankersCalculator.server.calculator.dtiCalc.calculator.DtiCalculator;
import com.bankersCalculator.server.calculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcResponse;
import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class DtiCalcService {


    /*
     * 프로그램 설명 :DTI 계산기 Service
     * 개발자 : 제갈명필
     * 수정  : 2024-07-13, 리펙토링.
     */
	
	@Autowired
    private DtiCalculator dtiCalculator;

    public DtiCalcResponse dticalculate(DtiCalcServiceRequest request) {
        double totalDtiAmount = 0;
        int annualIncome = request.getAnnualIncome();
        int totalLoanCount = 0;
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        

        
        List<DtiCalcResult> dtiCalcResultList = new ArrayList<>();
        for (DtiCalcServiceRequest.LoanStatus loanStatus : request.getLoanStatusList()) {
            //입력값 체크 -> 투입값 정상
        	//System.out.println(loanStatus.getLoanType());
        	//System.out.println(loanStatus.getRepaymentType());
        	
        	DtiCalcResult dtiCalcResult = dtiCalculator.calculateDti(loanStatus);

            dtiCalcResult.setSerial(++totalLoanCount);
            totalDtiAmount += dtiCalcResult.getAnnualPrincipalRepayment();
            totalDtiAmount += dtiCalcResult.getAnnualInterestRepayment();
            
         	
            System.out.println(decimalFormat.format(totalDtiAmount));

            dtiCalcResultList.add(dtiCalcResult);
        }

        double totalDtiRatio = (totalDtiAmount / annualIncome) * 100;

        return DtiCalcResponse.builder()
            .annualIncome(annualIncome)
            .totalLoanCount(totalLoanCount)
            .dtiCalcResultList(dtiCalcResultList)
            .finalDtiRatio(totalDtiRatio)
            .build();
    }
}
