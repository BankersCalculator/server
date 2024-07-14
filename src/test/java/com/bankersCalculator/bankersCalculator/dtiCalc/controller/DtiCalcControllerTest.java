package com.bankersCalculator.bankersCalculator.dtiCalc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcResponse;
import com.bankersCalculator.bankersCalculator.dtiCalc.service.DtiCalcService;

public class DtiCalcControllerTest {
	/*
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private DtiCalcService dtiCalcService;

    @Test
    public void testShowCalcForm() throws Exception {
        mockMvc.perform(get("/dti"))
                .andExpect(status().isOk())
                .andExpect(view().name("DtiCalc"))
                .andExpect(model().attributeExists("dtiCalcRequest"));
    }

    @Test
    public void testDtiCalculate() throws Exception {
        DtiCalcResponse response = new DtiCalcResponse();
        
        when(dtiCalcService.dticalculate(any())).thenReturn(response);

        mockMvc.perform(post("/dti/result")
                .param("annualIncome", "1000000")
                .param("loanStatusList[0].principal", "5000000"))
                .andExpect(status().isOk())
                .andExpect(view().name("DtiCalc"))
                .andExpect(model().attributeExists("dtiCalcResponse"));
    }
    */

}
