package com.bankersCalculator.bankersCalculator.dtiCalc.cotroller;

import com.example.dti.dto.DtiCalcRequest;
import com.example.dti.dto.DtiCalcResponse;
import com.example.dti.service.DtiCalcService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller

public class DtiCalcController {
	 @GetMapping("/dti-calc")
	 public String showCalcForm(Model model) {
		 model.addAttribute("dtiCalcRequest", new DtiCalcRequest());
		 return "DtiCalc";
	 }

	 @PostMapping("/calculate-dti")
	 public String calculateDti(DtiCalcRequest request, Model model) {
		 DtiCalcResponse response = dtiCalcService.calculateDti(request);
	     model.addAttribute("dtiCalcResponse", response);
	     return "DtiCalcResult";

	 }
 }