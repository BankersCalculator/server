package com.bankersCalculator.server.calculator.dtiCalc.service;

import com.bankersCalculator.server.calculator.dtiCalc.calculator.DtiCalculator;
import com.bankersCalculator.server.calculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcResponse;
import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcServiceRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DtiCalcService {
    private final DtiCalculator dtiCalculator;
    public DtiCalcService(DtiCalculator dtiCalculator){
        this.dtiCalculator = dtiCalculator;
    }

    public DtiCalcResponse dtiCalculate(DtiCalcServiceRequest request) {
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

        double totalDtiRatio = (totalDtiAmount / annualIncome * 100);

        return DtiCalcResponse.builder()
            .annualIncome(annualIncome)
            .totalLoanCount(totalLoanCount)
            .dtiCalcResults(dtiCalcResultList)
            .finalDtiRatio(totalDtiRatio)
            .build();
    }
}
