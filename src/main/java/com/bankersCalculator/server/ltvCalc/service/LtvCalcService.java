package com.bankersCalculator.server.ltvCalc.service;

import com.bankersCalculator.server.common.enums.ltv.HousingType;
import com.bankersCalculator.server.common.enums.ltv.RegionType;
import com.bankersCalculator.server.ltvCalc.dto.LtvCalcServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LtvCalcService {

    /***
     *
     * @param request
     * 어떻게 구현할까...
     *
     * 단순 LTV 계산기 기능과 LTV에 따른 최대한도를 계산하는 기능을 나눠야 할 것 같다.
     * 우선 계산기부터 구현하자
     *
     * -- 1. LTV 계산기
     * 1. 대출금액, 담보가치 등 정보를 토대로 단순 산출..
     * 2. 지역별 소액보증금 조회하는 로직이 필요함
     * 3. 최우선변제금액 산출 로직도 필요
     *
     *
     * -- 2. LTV에 따른 최대 한도
     * 그러나.. MVP인 전세대출 추천 서비스에서는 필요 없을 것 같다.
     * 추후에 주담대를 구현할 때 만들어도 될듯.
     * 1. 거주지역에 따른 투기과열/조정대상/기타지역 산정
     * 2. 주택가격, 주택보유수, 부동산관리지역타입을 토대로 지역별 LTV 계산
     * 3. LTV에 따른 최대 한도 계산
     */
    public void ltvCalculate(LtvCalcServiceRequest request) {
        double loanAmount = request.getLoanAmount();
        double priorMortgage = request.getPriorMortgage();

        double topPriorityRepaymentAmount = getTopPriorityRepaymentAmount(request);

        double ltvRatio = calculateLtvRatio(request, topPriorityRepaymentAmount);


    }

    private double getTopPriorityRepaymentAmount(LtvCalcServiceRequest request) {
        double collateralValue = request.getCollateralValue();
        HousingType housingType = request.getHousingType();
        int numbersOfRooms = request.getNumbersOfRooms();
        RegionType regionType = request.getRegionType();
        double smallAmountLeaseDeposit = regionType.getSmallAmountLeaseDeposit();


        double topPriorityRepaymentAmount = 0.0;
        double maximumRepaymentAmount = collateralValue / 2;

        if (housingType == HousingType.APARTMENT) {
            topPriorityRepaymentAmount = smallAmountLeaseDeposit;
        } else {
            topPriorityRepaymentAmount = numbersOfRooms * smallAmountLeaseDeposit;
        }

        return Math.min(topPriorityRepaymentAmount, maximumRepaymentAmount);
    }

    private double calculateLtvRatio(LtvCalcServiceRequest request, double topPriorityRepaymentAmount) {
        double collateralValue = request.getCollateralValue();
        double loanAmount = request.getLoanAmount();
        double currentLeaseDeposit = request.getCurrentLeaseDeposit();

        return (loanAmount + currentLeaseDeposit + topPriorityRepaymentAmount) / collateralValue;

    }
}
