package com.myZipPlan.server.calculator.dsrCalc.service;

import com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculator;
import com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculatorFactory;
import com.myZipPlan.server.calculator.dsrCalc.domain.DsrCalcResult;
import com.myZipPlan.server.calculator.dsrCalc.dto.DsrCalcResponse;
import com.myZipPlan.server.calculator.dsrCalc.dto.DsrCalcServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DsrCalcService {

    private final DsrCalculatorFactory dsrCalculatorFactory;

    /***
     *
     * @param request
     * @return DsrCalcResponse
     *
     * TODO: 개발이 필요한 사항
     * 1. 스트레스 금리 적용
     * 2. 초년도 기준(필요없을지도?)
     */
    public DsrCalcResponse dsrCalculate(DsrCalcServiceRequest request) {
        double totalDsrAmount = 0;
        int annualIncome = request.getAnnualIncome();
        int totalLoanCount = 0;

        List<DsrCalcResult> dsrCalcResultList = new ArrayList<>();
        for (DsrCalcServiceRequest.LoanStatus loanStatus : request.getLoanStatusList()) {
            DsrCalculator calculator = dsrCalculatorFactory.getCalculator(loanStatus.getLoanType());
            DsrCalcResult dsrCalcResult = calculator.calculateDsr(loanStatus);

            dsrCalcResult.setSerial(++totalLoanCount);
            totalDsrAmount += dsrCalcResult.getAnnualPrincipalRepayment();
            totalDsrAmount += dsrCalcResult.getAnnualInterestRepayment();

            dsrCalcResultList.add(dsrCalcResult);
        }

        double totalDsrRatio = (totalDsrAmount / annualIncome);

        return DsrCalcResponse.builder()
            .annualIncome(annualIncome)
            .totalLoanCount(totalLoanCount)
            .dsrCalcResults(dsrCalcResultList)
            .finalDsrRatio(totalDsrRatio)
            .build();
    }
}
