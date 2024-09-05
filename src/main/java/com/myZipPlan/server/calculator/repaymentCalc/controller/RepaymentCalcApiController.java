package com.myZipPlan.server.calculator.repaymentCalc.controller;

import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcRequest;
import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.myZipPlan.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.myZipPlan.server.common.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/repaymentCalc")
@RestController
public class RepaymentCalcApiController {

    private final RepaymentCalcService repaymentCalcService;

    @PostMapping
    public ApiResponse<RepaymentCalcResponse> calculateRepayment(@Valid @RequestBody RepaymentCalcRequest request) {
        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(request.toServiceRequest());
        return ApiResponse.ok(repaymentCalcResponse);
    }
}
