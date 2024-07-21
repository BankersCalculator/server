package com.bankersCalculator.server.repaymentCalc.controller;

import com.bankersCalculator.server.repaymentCalc.dto.RepaymentCalcRequest;
import com.bankersCalculator.server.repaymentCalc.dto.RepaymentCalcResponse;
import com.bankersCalculator.server.repaymentCalc.service.RepaymentCalcService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/repaymentCalc")
public class RepaymentCalcApiController {

    private final RepaymentCalcService repaymentCalcService;

    @PostMapping
    public ResponseEntity<RepaymentCalcResponse> calculateRepayment(@ModelAttribute @Valid RepaymentCalcRequest request) {
        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(request.toServiceRequest());
        return ResponseEntity.ok(repaymentCalcResponse);
    }
}
