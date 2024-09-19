package com.myZipPlan.server.docs.calculator;

import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.calculator.dtiCalc.controller.DtiCalcApiController;
import com.myZipPlan.server.calculator.dtiCalc.dto.DtiCalcRequest;
import com.myZipPlan.server.calculator.dtiCalc.dto.DtiCalcResponse;
import com.myZipPlan.server.calculator.dtiCalc.service.DtiCalcService;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DtiCalcApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/dtiCalc";
    private final DtiCalcService dtiCalcService = mock(DtiCalcService.class);

    @Override
    protected Object initController() {
        return new DtiCalcApiController(dtiCalcService);
    }

    @DisplayName("DTI 계산기 API")
    @Test
    void calculateDti() throws Exception {

        // 요청 객체 생성
        DtiCalcRequest request = DtiCalcRequest.builder()
            .annualIncome(BigDecimal.valueOf(100000000))
            .loanAmount(BigDecimal.valueOf(300000000))
            .interestRate(BigDecimal.valueOf(3.5))
            .loanTerm(360)
            .repaymentType(RepaymentType.AMORTIZING)
            .yearlyLoanInterestRepayment(BigDecimal.valueOf(10500000))
            .build();


        // 응답 객체 생성
        DtiCalcResponse response = DtiCalcResponse.builder()
            .dtiRatio(BigDecimal.valueOf(0.35))
            .annualIncome(BigDecimal.valueOf(100000000))
            .annualRepaymentAmount(BigDecimal.valueOf(21000000))
            .annualRepaymentPrincipal(BigDecimal.valueOf(10500000))
            .annualRepaymentInterest(BigDecimal.valueOf(10500000))
            .yearlyLoanInterestRepayment(BigDecimal.valueOf(10500000))
            .build();


        when(dtiCalcService.dtiCalculate(any()))
            .thenReturn(response);

        // MockMvc 수행 및 문서화
        mockMvc.perform(post(BASE_URL)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("calculator/dti-calc",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("annualIncome").type(JsonFieldType.NUMBER)
                        .description("연간 소득"),
                    fieldWithPath("loanAmount").type(JsonFieldType.NUMBER)
                        .description("대출 금액"),
                    fieldWithPath("interestRate").type(JsonFieldType.NUMBER)
                        .description("이자율"),
                    fieldWithPath("loanTerm").type(JsonFieldType.NUMBER)
                        .description("대출 기간 (개월)"),
                    fieldWithPath("repaymentType").type(JsonFieldType.STRING)
                        .description("상환 유형:\n" +
                            "  BULLET: 일시상환\n" +
                            "  AMORTIZING: 원리금균등분할상환\n" +
                            "  EQUAL_PRINCIPAL: 원금균등분할상환"),
                    fieldWithPath("yearlyLoanInterestRepayment").type(JsonFieldType.NUMBER)
                        .description("보유대출 연이자 상환액")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING)
                        .description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.dtiRatio").type(JsonFieldType.NUMBER)
                        .description("DTI 비율"),
                    fieldWithPath("data.annualIncome").type(JsonFieldType.NUMBER)
                        .description("연간 소득"),
                    fieldWithPath("data.annualRepaymentAmount").type(JsonFieldType.NUMBER)
                        .description("연간 원리금 상환액"),
                    fieldWithPath("data.annualRepaymentPrincipal").type(JsonFieldType.NUMBER)
                        .description("연간 원금 상환액"),
                    fieldWithPath("data.annualRepaymentInterest").type(JsonFieldType.NUMBER)
                        .description("연간 이자 상환액"),
                    fieldWithPath("data.yearlyLoanInterestRepayment").type(JsonFieldType.NUMBER)
                        .description("보유대출 연이자 상환액")
                )
            ));
    }
}
