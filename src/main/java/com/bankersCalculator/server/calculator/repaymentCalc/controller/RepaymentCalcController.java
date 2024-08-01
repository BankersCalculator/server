package com.bankersCalculator.server.calculator.repaymentCalc.controller;

import com.bankersCalculator.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.bankersCalculator.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.bankersCalculator.server.calculator.repaymentCalc.dto.RepaymentCalcRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/repaymentCalc")
@Slf4j
public class RepaymentCalcController {

    private final RepaymentCalcService repaymentCalcService;

    @GetMapping
    public String calculateRepaymentFrom(Model model) {
        model.addAttribute("repaymentCalcRequest", new RepaymentCalcRequest());
        return "repaymentCalc/repaymentCalc";
    }

    @PostMapping
    public String calculateRepayment(@ModelAttribute @Valid RepaymentCalcRequest request,
                                     Model model) {
        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculateRepayment(request.toServiceRequest());
        model.addAttribute(repaymentCalcResponse);
        return "repaymentCalc/repaymentCalcResult";
    }
}
