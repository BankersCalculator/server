package com.bankersCalculator.bankersCalculator.dsrCalc.cotroller;

import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcRequest;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcResponse;
import com.bankersCalculator.bankersCalculator.dsrCalc.service.DsrCalcService;
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
@Slf4j
@RequestMapping("/dsr")
public class DsrCalcController {

    private final DsrCalcService dsrCalcService;

    @GetMapping
    public String dsrMain(Model model) {
        //TODO: 초기화가 꼭 필요한 걸까?
        model.addAttribute("dsrCalcRequest", new DsrCalcRequest());
        return "dsr/calc";
    }

    @PostMapping("/calc")
    public String dsrCalculate(@ModelAttribute("dsrCalcRequest") DsrCalcRequest dsrCalcRequest, Model model) {
        DsrCalcResponse dsrCalcResponse = dsrCalcService.dsrCalculate(dsrCalcRequest);
        model.addAttribute("dsrCalcResponse", dsrCalcResponse);
        return "dsr/calc";
    }
}


