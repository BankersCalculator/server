package com.bankersCalculator.server.calculator.dsrCalc.service;

import com.bankersCalculator.server.calculator.dsrCalc.calculator.DsrCalculator;
import com.bankersCalculator.server.calculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.server.calculator.dsrCalc.dto.DsrCalcResponse;
import com.bankersCalculator.server.calculator.dsrCalc.dto.DsrCalcServiceRequest;
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
     * TODO: 확인이 필요한 사항
     * 1. 주담대 원금균등상환 - 거치기간 존재할 경우.
     *      본 프로그램은 거치기간을 고려한 repaymentCalc의 총이자를 기준으로 계산하나
     *      부동산계산기는 이자계산 시 거치기간 없는 원금균등상환액의 이자를 기준으로 계산함.
     *
     *      주담대 균등상환 만기상환액이 있을 경우, 부동산계산기의 연이자상환액 산식 이상함
     *      균등분할이자산출 * (만기상환액/원금)을 하는데.. 좀 이상한듯
     *
     * 2. 전세대출 원금/원리금균등분할상환
     *      본 프로그램은 전세일 경우 상환구분 없이 연이자율을 곱한 값을 이자액으로 산출하나
     *      부동산계산기는 원금/원리금 계산 후 이자합산액을 기간으로 나눠서 계산함.
     *
     * TODO: 개발이 필요한 사항
     * 1. 스트레스 금리 적용
     * 2. 초년도 기준(필요없을지도?)
     * 3. 신DTI 구현(마찬가지로 필요없을 것 같다.)
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
