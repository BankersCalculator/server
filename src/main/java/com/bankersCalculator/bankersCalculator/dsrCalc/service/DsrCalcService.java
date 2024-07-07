package com.bankersCalculator.bankersCalculator.dsrCalc.service;

import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcResponse;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DsrCalcService {

    private final DsrCalculatorFactory dsrCalculatorFactory;

    public DsrCalcResponse dsrCalculate(DsrCalcServiceRequest dsrCalcServiceRequest) {

        DsrCalcResponse dsrCalcResponse = dsrCalculatorFactory.calcTotalDsr(dsrCalcServiceRequest);

        return dsrCalcResponse;
    }
}
