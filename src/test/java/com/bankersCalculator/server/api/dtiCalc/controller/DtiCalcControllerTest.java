package com.bankersCalculator.server.api.dtiCalc.controller;

import com.bankersCalculator.server.calculator.dtiCalc.controller.DtiCalcController;
import com.bankersCalculator.server.calculator.dtiCalc.service.DtiCalcService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 어노테이션은 스프링 MVC 컨트롤러를 테스트하기 위해 사용됩니다. 이 어노테이션은 컨트롤러와 관련된 빈만 로드하여 테스트의 범위를 좁
// 적용 대상: DtiCalcController.class를 지정하여 DtiCalcController를 테스트합니다.
@WebMvcTest(DtiCalcController.class)
public class DtiCalcControllerTest {	
	//스프링 컨텍스트에서 관리되는 빈을 주입받기 위해 사용
	@Autowired
    private MockMvc mockMvc;
    
	//용도: MockMvc를 사용하여 컨트롤러의 엔드포인트를 테스트합니다.
    @MockBean
    private DtiCalcService dtiCalcService; /// DtiCalcService의 모의 객체를 주입받아 사용
    
    

    @Test
    public void testShowCalcForm() throws Exception {
    	//dti URL에 대해 get 요청 수행 
        mockMvc.perform(get("/dti"))
                //HTTP 상태코드 200인지 확
                .andExpect(status().isOk())
                //반환 명칭 확
                .andExpect(view().name("DtiCalc"))
                //모델에 dtiCalcRequest 속성있는지 존재유무 확인 
                .andExpect(model().attributeExists("dtiCalcRequest"));
    }
    
    
    //목적: POST 요청을 통해 DTI 계산 결과를 처리하는 컨트롤러 메서드를 테스트합니다.
//    @Test
//    public void testDtiCalculate() throws Exception {
//    	// DtiCalcResponse는 빌더패턴을 사용하기 때문에, 빌더를 사용한 객체생성이 필요함.
//        //DtiCalcResponse response = new DtiCalcResponse();
//    	DtiCalcResponse response = DtiCalcResponse.builder()
//    		    .annualIncome(0)
//    		    .totalLoanCount(0)
//    		    .finalDtiRatio(0.0)
//    		    .dtiCalcResultList(new ArrayList<>())
//    		    .build();
//        //dtiCalcService.dticalculate 메서드가 호출될 때 모의 객체가 response를 반환하도록 설정
//        when(dtiCalcService.dticalculate(any())).thenReturn(response);
//
//        // /dti/result URL에 대해 POST 요청을 수행합니다
//        mockMvc.perform(post("/dti/result")
//
//
//        	.param("annualIncome", "1000000")
//        	.param("loanStatusList[0].repaymentType", RepaymentType.BULLET.name())
//        	.param("loanStatusList[0].loanType", LoanType.MORTGAGE.name())
//        	.param("loanStatusList[0].principal", "5000000")
//        	.param("loanStatusList[0].maturityPaymentAmount", "1000000")
//        	.param("loanStatusList[0].term", "30")
//        	.param("loanStatusList[0].gracePeriod", "5")
//        	.param("loanStatusList[0].interestRatePercentage", "3.5"))
//
//        	.andExpect(status().isOk())
//            .andExpect(view().name("DtiCalc"))
//            .andExpect(model().attributeExists("dtiCalcResponse"));
//    }
    
    
    
}
