package com.bankersCalculator.server.advice.loanAdvice.controller;

import com.bankersCalculator.server.advice.loanAdvice.dto.api.LoanAdviceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.api.LoanAdviceResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.api.LoanAdviceSummaryResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.api.SpecificLoanAdviceRequest;
import com.bankersCalculator.server.advice.loanAdvice.service.LoanAdviceService;
import com.bankersCalculator.server.common.api.ApiResponse;
import jakarta.validation.Valid;
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
    public ApiResponse<LoanAdviceResponse> generateLoanAdvice(@RequestBody @Valid LoanAdviceRequest request) {
        LoanAdviceResponse loanAdviceResponse = loanAdviceService.generateLoanAdvice(request.toServiceRequest());

        return ApiResponse.ok(loanAdviceResponse);
    }

    @PostMapping("/specific")
    public ApiResponse<LoanAdviceResponse> generateLoanAdviceOnSpecificLoan(@RequestBody SpecificLoanAdviceRequest request) {
        LoanAdviceResponse loanAdviceResponse = loanAdviceService.generateLoanAdviceOnSpecificLoan(request.getLoanAdviceResultId(), request.getProductCode());

        return ApiResponse.ok(loanAdviceResponse);
    }
}
