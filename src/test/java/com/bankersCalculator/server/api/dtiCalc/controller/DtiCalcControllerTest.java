package com.bankersCalculator.server.api.dtiCalc.controller;

import com.bankersCalculator.server.ControllerTestSupport;
import com.bankersCalculator.server.calculator.dtiCalc.controller.DtiCalcController;
import com.bankersCalculator.server.calculator.dtiCalc.service.DtiCalcService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 어노테이션은 스프링 MVC 컨트롤러를 테스트하기 위해 사용됩니다. 이 어노테이션은 컨트롤러와 관련된 빈만 로드하여 테스트의 범위를 좁
// 적용 대상: DtiCalcController.class를 지정하여 DtiCalcController를 테스트합니다.
public class DtiCalcControllerTest extends ControllerTestSupport {

    @Test
    public void testShowCalcForm() throws Exception {
      
    	//dti URL에 대해 get 요청 수행
        mockMvc.perform(get("/dti")
                .with(csrf())
            )
                //HTTP 상태코드 200인지 확
                .andExpect(status().isOk())
                //반환 명칭 확
                .andExpect(view().name("DtiCalc"))
                //모델에 dtiCalcRequest 속성있는지 존재유무 확인
                .andExpect(model().attributeExists("dtiCalcRequest"));
    }
}
