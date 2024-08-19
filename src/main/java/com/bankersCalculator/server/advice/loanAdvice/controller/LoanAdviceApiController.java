package com.bankersCalculator.server.advice.loanAdvice.controller;

import com.bankersCalculator.server.advice.loanAdvice.dto.LoanAdviceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.LoanAdviceResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.LoanAdviceSummaryResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.SpecificLoanAdviceRequest;
import com.bankersCalculator.server.advice.loanAdvice.service.LoanAdviceService;
import com.bankersCalculator.server.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/loanAdvice")
@RestController
public class LoanAdviceApiController {

    private final LoanAdviceService loanAdviceService;


    @GetMapping
    public ApiResponse<List<LoanAdviceSummaryResponse>> getRecentLoanAdvices() {
        List<LoanAdviceSummaryResponse> recentLoanAdvices = loanAdviceService.getRecentLoanAdvices();

        return ApiResponse.ok(recentLoanAdvices);
    }

    @GetMapping("/specific/{loanAdviceResultId}")
    public ApiResponse<LoanAdviceResponse> getSpecificLoanAdvice(@PathVariable Long loanAdviceResultId) {
        LoanAdviceResponse specificLoanAdvice = loanAdviceService.getSpecificLoanAdvice(loanAdviceResultId);

        return ApiResponse.ok(specificLoanAdvice);
    }

    @PostMapping
    public ApiResponse<LoanAdviceResponse> generateLoanAdvice(@RequestBody LoanAdviceRequest request) {
        LoanAdviceResponse loanAdviceResponse = loanAdviceService.generateLoanAdvice(request.toServiceRequest());

        return ApiResponse.ok(loanAdviceResponse);
    }

    @PostMapping("/{productCode}")
    public ApiResponse<LoanAdviceResponse> generateLoanAdviceOnSpecificLoan(@PathVariable String productCode,
                                                                            @RequestBody SpecificLoanAdviceRequest request) {
        LoanAdviceResponse loanAdviceResponse = loanAdviceService.generateLoanAdviceOnSpecificLoan(productCode, request.getUserId(), request.getLoanAdviceResultId());

        return ApiResponse.ok(loanAdviceResponse);
    }
}
