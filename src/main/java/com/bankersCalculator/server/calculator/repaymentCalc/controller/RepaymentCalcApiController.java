package com.bankersCalculator.server.calculator.repaymentCalc.controller;

import com.bankersCalculator.server.calculator.repaymentCalc.dto.RepaymentCalcRequest;
import com.bankersCalculator.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.bankersCalculator.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.bankersCalculator.server.common.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/repaymentCalc")
public class RepaymentCalcApiController {

    private final RepaymentCalcService repaymentCalcService;

    @PostMapping
    public ApiResponse<RepaymentCalcResponse> calculateRepayment(@Valid @RequestBody RepaymentCalcRequest request) {
        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(request.toServiceRequest());
        return ApiResponse.ok(repaymentCalcResponse);
    }
}
