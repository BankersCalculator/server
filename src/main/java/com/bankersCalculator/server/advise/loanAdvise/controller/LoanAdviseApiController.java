package com.bankersCalculator.server.advise.loanAdvise.controller;

import com.bankersCalculator.server.advise.loanAdvise.dto.LoanAdviseRequest;
import com.bankersCalculator.server.advise.loanAdvise.dto.UserInputInfoRequest;
import com.bankersCalculator.server.advise.loanAdvise.dto.UserInputInfoResponse;
import com.bankersCalculator.server.advise.loanAdvise.service.LoanAdviseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/loanAdvise")
@RestController
public class LoanAdviseApiController {

    private final LoanAdviseService loanAdviseService;

    @GetMapping
    public UserInputInfoResponse getSubmittedUserInput(UserInputInfoRequest request) {
        loanAdviseService.getSubmittedUserInput(request.toServiceRequest());
        return null;
    }

    @PostMapping
    public void generateLoanAdvise(LoanAdviseRequest request) {
        loanAdviseService.generateLoanAdvise(request.toServiceRequest());
    }

    @PostMapping
    public void generateLoanAdviseOnSpecificLoan(LoanAdviseRequest request) {
        loanAdviseService.generateLoanAdviseOnSpecificLoan(request.toServiceRequest());
    }



}
