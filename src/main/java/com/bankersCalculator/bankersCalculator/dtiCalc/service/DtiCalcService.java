package com.bankersCalculator.bankersCalculator.dtiCalc.service;


import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcRequest;
import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcResponse;
import org.springframework.stereotype.Service;

@Service
public class DtiCalcService {

    public DtiCalcResponse calculateDti(DtiCalcRequest request) {
        DtiCalcResponse response = new DtiCalcResponse();
        double annualPrincipalAndInterest = request.getRepaymentAmount() + request.getSavingsLoanAmount() + request.getMortgageLoanAmount() + request.getOtherDebtsTotalAmount();
        double dtiRatio = (annualPrincipalAndInterest / request.getAnnualIncome()) * 100;

        response.setAnnnualPrincipalAndInterest(annualPrincipalAndInterest);
        response.setDtiRatio(dtiRatio);

        return response;
    }
}