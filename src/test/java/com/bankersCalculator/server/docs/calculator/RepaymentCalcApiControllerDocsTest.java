package com.bankersCalculator.server.docs.calculator;

import com.bankersCalculator.server.RestDocsSupport;
import com.bankersCalculator.server.calculator.repaymentCalc.controller.RepaymentCalcApiController;
import com.bankersCalculator.server.calculator.repaymentCalc.domain.RepaymentSchedule;
import com.bankersCalculator.server.calculator.repaymentCalc.dto.RepaymentCalcRequest;
import com.bankersCalculator.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.bankersCalculator.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.bankersCalculator.server.common.enums.RepaymentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RepaymentCalcApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/repaymentCalc";
    private final RepaymentCalcService repaymentCalcService = mock(RepaymentCalcService.class);

    @Override
    protected Object initController() {
        return new RepaymentCalcApiController(repaymentCalcService);
    }


    @DisplayName("원리금 계산기 API")
    @Test
    void calculateRepayment() throws Exception {
        RepaymentCalcRequest request = RepaymentCalcRequest.builder()
            .repaymentType(RepaymentType.AMORTIZING)
            .principal(300000000)
            .term(60)
            .gracePeriod(0)
            .interestRatePercentage(4)
            .maturityPaymentAmount(0)
            .build();

        RepaymentCalcResponse response = RepaymentCalcResponse.builder()
            .repaymentScheduleList(Arrays.asList(
                new RepaymentSchedule(1, 1000000, 5000000, 500000, 295000000),
                new RepaymentSchedule(2, 2000000, 7000000, 450000, 200000000)
            ))
            .totalPrincipal(300000000.0)
            .totalInterest(3500000.0)
            .totalInstallments(60)
            .build();
        when(repaymentCalcService.calculateRepayment(any()))
            .thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("calculator/repayment-calc",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("repaymentType").type(JsonFieldType.STRING)
                        .description("상환 유형"),
                    fieldWithPath("principal").type(JsonFieldType.NUMBER)
                        .description("원금"),
                    fieldWithPath("term").type(JsonFieldType.NUMBER)
                        .description("기간(개월수)"),
                    fieldWithPath("gracePeriod").type(JsonFieldType.NUMBER)
                        .description("거치기간"),
                    fieldWithPath("interestRatePercentage").type(JsonFieldType.NUMBER)
                        .description("연이자율(%)"),
                    fieldWithPath("maturityPaymentAmount").type(JsonFieldType.NUMBER)
                        .description("만기상환금액")
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
                    fieldWithPath("data.repaymentScheduleList").type(JsonFieldType.ARRAY)
                        .description("상환 스케줄 목록"),
                    fieldWithPath("data.repaymentScheduleList[].installmentNumber").type(JsonFieldType.NUMBER)
                        .description("할부 순번"),
                    fieldWithPath("data.repaymentScheduleList[].totalPayment").type(JsonFieldType.NUMBER)
                        .description("상환 총액"),
                    fieldWithPath("data.repaymentScheduleList[].principalPayment").type(JsonFieldType.NUMBER)
                        .description("원금 상환액"),
                    fieldWithPath("data.repaymentScheduleList[].interestPayment").type(JsonFieldType.NUMBER)
                        .description("이자 상환액"),
                    fieldWithPath("data.repaymentScheduleList[].remainingPrincipal").type(JsonFieldType.NUMBER)
                        .description("잔여 원금"),
                    fieldWithPath("data.totalPrincipal").type(JsonFieldType.NUMBER)
                        .description("총 원금"),
                    fieldWithPath("data.totalInterest").type(JsonFieldType.NUMBER)
                        .description("총 이자"),
                    fieldWithPath("data.totalInstallments").type(JsonFieldType.NUMBER)
                        .description("총 상환회차")
                )
            ));
    }
}
