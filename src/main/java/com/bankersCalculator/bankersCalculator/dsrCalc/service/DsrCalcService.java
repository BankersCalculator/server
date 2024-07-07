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

    // TODO: dsrCalcRequest -> ServiceRequest로 변환할 것.
    public DsrCalcResponse dsrCalculate(DsrCalcServiceRequest dsrCalcServiceRequest) {

        dsrCalculatorFactory.calcTotalDsr(dsrCalcServiceRequest);




        return null;
    }
}
