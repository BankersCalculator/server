package com.bankersCalculator.bankersCalculator.dsrCalc.service;

import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcRequest;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DsrCalcService {
    public DsrCalcResponse dsrCalculate(DsrCalcRequest dsrCalcRequest) {
        String name = dsrCalcRequest.getTestName();
        int firstValue = dsrCalcRequest.getFirstValue();

        int calcValue = firstValue * 300;

        DsrCalcResponse response = DsrCalcResponse.builder()
            .name(name)
            .firstValue(calcValue)
            .build();

        log.info("dsrCalcService 실행: {}", response.getFirstValue());

        return response;
    }
}
