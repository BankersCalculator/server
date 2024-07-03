package com.bankersCalculator.bankersCalculator.dtiCalc.controller;

import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcRequest;
import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcResponse;
import com.bankersCalculator.bankersCalculator.dtiCalc.service.DtiCalcService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DtiCalcController {

    @Autowired
    private DtiCalcService dtiCalcService;

    @GetMapping("/dti-calc")
    public String showCalcForm(Model model) {
        model.addAttribute("dtiCalcRequest", new DtiCalcRequest());
        return "DtiCalc";
    }

    @PostMapping("/calculate-dti")
    public String calculateDti(@ModelAttribute DtiCalcRequest request, Model model) {
        DtiCalcResponse response = dtiCalcService.calculateDti(request);
        model.addAttribute("dtiCalcResponse", response);
        return "DtiCalcResult";
    }
}
