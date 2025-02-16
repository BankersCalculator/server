package com.myZipPlan.server.advice.rateProvider.service;

import com.myZipPlan.server.common.enums.loanAdvice.BaseRate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RateProviderService {

    /**
     * TODO: 실제 금리를 조회하는 로직 구현할 것.
     *  고시된 금리를 자체 DB 적재하는 방법 고려
     *  우선 250205 기준 고시 금리로 세팅함.
     */


    public BigDecimal getBaseRate(BaseRate baseRate) {

        BigDecimal rate = BigDecimal.ZERO;

        if (baseRate == BaseRate.COFIX_NEW_DEAL) {
            rate = new BigDecimal("3.22");
        }
        if (baseRate == BaseRate.COFIX_NEW_BALANCE) {
            rate = new BigDecimal("2.98");
        }
        if (baseRate == BaseRate.COFIX_BALANCE) {
            rate = new BigDecimal("3.47");
        }

        if (baseRate == BaseRate.FINANCIAL_BOND_6M) {
            rate = new BigDecimal("2.99");
        }

        if (baseRate == BaseRate.FINANCIAL_BOND_24M) {
            rate = new BigDecimal("2.8");
        }

        return rate;
    }

    public BigDecimal getNewBornSpecialLeaseLoanRate(BigDecimal rentalDeposit, BigDecimal combinedIncome) {

        int depositRangeIndex = 0;
        if (rentalDeposit.compareTo(new BigDecimal("20000000")) > 0) {
            depositRangeIndex++;
        }
        if (rentalDeposit.compareTo(new BigDecimal("40000000")) > 0) {
            depositRangeIndex++;
        }
        if (rentalDeposit.compareTo(new BigDecimal("60000000")) > 0) {
            depositRangeIndex++;
        }
        if (rentalDeposit.compareTo(new BigDecimal("75000000")) > 0) {
            depositRangeIndex++;
        }
        if (rentalDeposit.compareTo(new BigDecimal("100000000")) > 0) {
            depositRangeIndex++;
        }

        int incomeRangeIndex = 0;
        if (combinedIncome.compareTo(new BigDecimal("50000000")) > 0) {
            incomeRangeIndex++;
        }
        if (combinedIncome.compareTo(new BigDecimal("100000000")) > 0) {
            incomeRangeIndex++;
        }
        if (combinedIncome.compareTo(new BigDecimal("150000000")) > 0) {
            incomeRangeIndex++;
        }

        BigDecimal[][] interestRates = {
            {new BigDecimal("1.10"), new BigDecimal("1.20"), new BigDecimal("1.30"), new BigDecimal("1.40")},
            {new BigDecimal("1.40"), new BigDecimal("1.50"), new BigDecimal("1.60"), new BigDecimal("1.70")},
            {new BigDecimal("1.70"), new BigDecimal("1.80"), new BigDecimal("1.90"), new BigDecimal("2.00")},
            {new BigDecimal("2.00"), new BigDecimal("2.10"), new BigDecimal("2.20"), new BigDecimal("2.30")},
            {new BigDecimal("2.35"), new BigDecimal("2.45"), new BigDecimal("2.55"), new BigDecimal("2.65")},
            {new BigDecimal("2.70"), new BigDecimal("2.80"), new BigDecimal("2.90"), new BigDecimal("3.00")}
        };

        BigDecimal finalRate = interestRates[depositRangeIndex][incomeRangeIndex];
        return finalRate.divide(new BigDecimal("100"));
    }
}
