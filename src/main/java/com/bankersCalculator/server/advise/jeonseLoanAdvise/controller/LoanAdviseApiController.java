package com.bankersCalculator.server.advise.jeonseLoanAdvise.controller;

import com.bankersCalculator.server.advise.jeonseLoanAdvise.dto.LoanAdviseRequest;
import com.bankersCalculator.server.advise.jeonseLoanAdvise.dto.UserInfoResponse;
import com.bankersCalculator.server.advise.jeonseLoanAdvise.service.LoanAdviseService;
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
    public UserInfoResponse getSubmittedUserInput() {
        return null;
    }


    @PostMapping
    public void generateLoanAdvise(LoanAdviseRequest request) {
        loanAdviseService.generateLoanAdvise(request.toServiceRequest());
    }

}
