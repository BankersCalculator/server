package com.myZipPlan.server.calculator.ltvCalc.service;

import com.myZipPlan.server.calculator.ltvCalc.dto.LtvCalcResponse;
import com.myZipPlan.server.calculator.ltvCalc.dto.LtvCalcServiceRequest;
import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.common.enums.calculator.LoanPurpose;
import com.myZipPlan.server.common.enums.calculator.RegionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.myZipPlan.server.common.enums.calculator.HouseOwnershipType.LIFETIME_FIRST;

@Service
@RequiredArgsConstructor
@Slf4j
public class LtvCalcService {

    private final LtvMatrix ltvMatrix;

    public LtvCalcResponse calculate(LtvCalcServiceRequest request) {

        LoanPurpose loanPurpose = request.getLoanPurpose();
        BigDecimal collateralValue = request.getCollateralValue();
        RegionType regionType = request.getRegionType();
        HouseOwnershipType houseOwnershipType = request.getHouseOwnershipType();
        boolean isLifetimeFirst = houseOwnershipType == LIFETIME_FIRST;

        BigDecimal ltvRatio = ltvMatrix.getLtvRatio(loanPurpose, regionType, houseOwnershipType);
        BigDecimal possibleLoanAmount = calculatePossibleLoanAmount(loanPurpose, isLifetimeFirst, collateralValue, ltvRatio);

        return LtvCalcResponse.builder()
            .ltvRatio(ltvRatio)
            .collateralValue(collateralValue)
            .possibleLoanAmount(possibleLoanAmount)
            .build();
    }

    private BigDecimal calculatePossibleLoanAmount(LoanPurpose loanPurpose,boolean isLifetimeFirst, BigDecimal collateralValue, BigDecimal ltvRatio) {

        if (loanPurpose == LoanPurpose.HOME_PURCHASE) {
            BigDecimal amount = collateralValue.multiply(ltvRatio);
            BigDecimal maxAmount = isLifetimeFirst ? BigDecimal.valueOf(600000000) : BigDecimal.valueOf(999999999999L);
            return amount.compareTo(maxAmount) > 0 ? maxAmount : amount;
        } else {
            BigDecimal amount = collateralValue.multiply(ltvRatio);
            BigDecimal maxAmount = BigDecimal.valueOf(200000000);
            return amount.compareTo(maxAmount) > 0 ? maxAmount : amount;
        }
    }

}
