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

    @GetMapping("/dti")
    public String showCalcForm(Model model) {
        model.addAttribute("dtiCalcRequest", new DtiCalcRequest());
        model.addAttribute("dtiCalcResponse", null); // 초기 상태에서는 결과가 없으므로 null로 설정
        return "DtiCalc";
    }

    @PostMapping("/dti/result")
    public String calculateDti(@ModelAttribute DtiCalcRequest request, Model model) {
        DtiCalcResponse response = dtiCalcService.calculateDti(request);
        model.addAttribute("dtiCalcRequest", request); // 폼 데이터를 유지
        model.addAttribute("dtiCalcResponse", response); // 결과 데이터 설정
        return "DtiCalc";
    }
}
