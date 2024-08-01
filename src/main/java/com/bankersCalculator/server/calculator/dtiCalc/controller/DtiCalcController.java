package com.bankersCalculator.server.calculator.dtiCalc.controller;

import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcRequest;
import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcResponse;
import com.bankersCalculator.server.calculator.dtiCalc.service.DtiCalcService;
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

        return "DtiCalc";
    }

    //action : 스프링 MVC에서 컨트롤러 메서드는 특정 URL 요청을 처리하는 "액션" 역할을 합니다.
    @PostMapping("/dti/result")
    public String dtiCalculate(@ModelAttribute("dtiCalcRequest") DtiCalcRequest request, Model model) {
        DtiCalcResponse response = dtiCalcService.dticalculate(request.toServiceRequest());
        model.addAttribute("dtiCalcResponse", response); // 결과 데이터 설정
        return "DtiCalc";
    }
}
