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

import java.math.BigDecimal;

@RequiredArgsConstructor
@RequestMapping("/api/v1/repaymentCalc")
@RestController
public class RepaymentCalcApiController {

    private final RepaymentCalcService repaymentCalcService;

    @PostMapping
    public ApiResponse<RepaymentCalcResponse> calculateRepayment(@Valid @RequestBody RepaymentCalcRequest request) {

        BigDecimal term = request.getTerm();
        BigDecimal gracePeriod = request.getGracePeriod();

        if (term.compareTo(gracePeriod) <= 0) {
            return ApiResponse.fail("거치기간은 대출기간보다 작아야 합니다.");
        }

        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculate(request.toServiceRequest());
        return ApiResponse.ok(repaymentCalcResponse);
    }
}
