package com.bankersCalculator.server.advice.rateProvider.service;

import com.bankersCalculator.server.common.enums.loanAdvice.BaseRate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RateProviderService {

    /**
     * TODO: 실제 금리를 조회하는 로직 구현할 것.
     *  고시된 금리를 자체 DB 적재하는 방법 고려
     *  우선 240826 기준 고시 금리로 세팅함.
     */


    public BigDecimal getBaseRate(BaseRate baseRate) {

        BigDecimal rate = BigDecimal.ZERO;

        if (baseRate == BaseRate.COFIX_NEW_DEAL) {
            rate = new BigDecimal("3.42");
        }
        if (baseRate == BaseRate.COFIX_NEW_BALANCE) {
            rate = new BigDecimal("3.15");
        }
        if (baseRate == BaseRate.COFIX_BALANCE) {
            rate = new BigDecimal("3.69");
        }

        return rate;
    }
}
