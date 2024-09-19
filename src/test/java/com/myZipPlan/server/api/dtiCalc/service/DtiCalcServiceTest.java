package com.myZipPlan.server.api.dtiCalc.service;

import com.myZipPlan.server.calculator.dtiCalc.dto.DtiCalcResponse;
import com.myZipPlan.server.calculator.dtiCalc.dto.DtiCalcServiceRequest;
import com.myZipPlan.server.calculator.dtiCalc.service.DtiCalcService;
import com.myZipPlan.server.common.enums.calculator.LoanType;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DecimalFormat;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DtiCalcServiceTest {

	@Autowired
    private DtiCalcService dtiCalcService;


    @DisplayName("DTI 계산 테스트")
    @Test
    void dtiCalcTest() throws Exception{
        //given

        //when

        //then
    }
}
