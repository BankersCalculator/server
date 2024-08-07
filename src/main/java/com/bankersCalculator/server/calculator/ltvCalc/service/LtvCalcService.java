package com.bankersCalculator.server.calculator.ltvCalc.service;

import com.bankersCalculator.server.calculator.ltvCalc.dto.LtvCalcResponse;
import com.bankersCalculator.server.calculator.ltvCalc.dto.LtvCalcServiceRequest;
import com.bankersCalculator.server.common.enums.ltv.HousingType;
import com.bankersCalculator.server.common.enums.ltv.RegionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LtvCalcService {

    public LtvCalcResponse ltvCalculate(LtvCalcServiceRequest request) {

        double topPriorityRepaymentAmount = getTopPriorityRepaymentAmount(request);
        double totalLoanExposure = calculateTotalLoanExposure(request, topPriorityRepaymentAmount);
        double ltvRatio = calculateLtvRatio(request.getCollateralValue(), totalLoanExposure);

        return request.toResponse(topPriorityRepaymentAmount, totalLoanExposure, ltvRatio);
    }


    // TODO: 방수차감기준 확인/수정할 것.
    private double getTopPriorityRepaymentAmount(LtvCalcServiceRequest request) {
        double totalSmallAmountLeaseDeposit = getTotalSmallAmountLeaseDeposit(request);
        double collateralValue = request.getCollateralValue();
        double maximumRepaymentAmount = collateralValue / 2;
        return Math.min(totalSmallAmountLeaseDeposit, maximumRepaymentAmount);
    }

    private double getTotalSmallAmountLeaseDeposit(LtvCalcServiceRequest request) {
        HousingType housingType = request.getHousingType();
        RegionType regionType = request.getRegionType();
        double smallAmountLeaseDeposit = regionType.getSmallAmountLeaseDeposit();
        int numberOfRooms = request.getNumberOfRooms();
        double totalSmallAmountLeaseDeposit;

        if (housingType == HousingType.APARTMENT) {
            totalSmallAmountLeaseDeposit = smallAmountLeaseDeposit;
        } else {
            totalSmallAmountLeaseDeposit = (numberOfRooms - 1) * smallAmountLeaseDeposit;
        }
        return totalSmallAmountLeaseDeposit;
    }

    private double calculateTotalLoanExposure(LtvCalcServiceRequest request, double topPriorityRepaymentAmount) {
        return request.getLoanAmount() + request.getCurrentLeaseDeposit() + topPriorityRepaymentAmount + request.getPriorMortgage();
    }

    private double calculateLtvRatio(double collateralValue, double totalLoanExposure) {
        log.info(String.valueOf(collateralValue));
        log.info(String.valueOf(totalLoanExposure));
        log.info(String.valueOf(totalLoanExposure / collateralValue));
        return totalLoanExposure / collateralValue;

    }
}
