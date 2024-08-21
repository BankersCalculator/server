package com.bankersCalculator.server.docs.calculator;

import com.bankersCalculator.server.RestDocsSupport;
import com.bankersCalculator.server.calculator.ltvCalc.controller.LtvCalcController;
import com.bankersCalculator.server.calculator.ltvCalc.dto.LtvCalcRequest;
import com.bankersCalculator.server.calculator.ltvCalc.dto.LtvCalcResponse;
import com.bankersCalculator.server.calculator.ltvCalc.service.LtvCalcService;
import com.bankersCalculator.server.common.enums.ltv.HousingType;
import com.bankersCalculator.server.common.enums.ltv.RegionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

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

public class LtvCalcApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/ltvCalc";
    private final LtvCalcService ltvCalcService = mock(LtvCalcService.class);

    @Override
    protected Object initController() {
        return new LtvCalcController(ltvCalcService);
    }

    @DisplayName("LTV 계산기 API")
    @Test
    void calculateLtv() throws Exception {
        LtvCalcRequest request = LtvCalcRequest.builder()
            .loanAmount(300000000.0)
            .collateralValue(500000000.0)
            .priorMortgage(50000000.0)
            .numberOfRooms(3)
            .housingType(HousingType.APARTMENT)
            .regionType(RegionType.SEOUL)
            .currentLeaseDeposit(20000000.0)
            .build();

        LtvCalcResponse response = LtvCalcResponse.builder()
            .loanAmount(300000000.0)
            .collateralValue(500000000.0)
            .priorMortgage(50000000.0)
            .numbersOfRooms(3)
            .smallAmountLeaseDeposit(55000000.0)
            .topPriorityRepaymentAmount(105000000.0)
            .totalLoanExposure(350000000.0)
            .ltvRatio(70.0)
            .build();

        when(ltvCalcService.ltvCalculate(any()))
            .thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("calculator/ltv-calc",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("loanAmount").type(JsonFieldType.NUMBER)
                        .description("대출금액"),
                    fieldWithPath("collateralValue").type(JsonFieldType.NUMBER)
                        .description("담보가치"),
                    fieldWithPath("priorMortgage").type(JsonFieldType.NUMBER)
                        .description("선순위채권"),
                    fieldWithPath("numberOfRooms").type(JsonFieldType.NUMBER)
                        .description("방 수"),
                    fieldWithPath("housingType").type(JsonFieldType.STRING)
                        .description("주택유형 (APARTMENT: 아파트, DETACHED_HOUSE: 단독주택, MULTI_FAMILY_HOUSE: 다가구주택, " +
                            "MULTI_HOUSEHOLD_HOUSE: 다세대주택, OFFICETEL: 오피스텔, OTHER: 기타)"),
                    fieldWithPath("regionType").type(JsonFieldType.STRING)
                        .description("지역 (SEOUL: 서울, CAPITAL_AREA: 수도권(+세종시), METROPOLITAN_CITY: 광역시, OTHER_AREAS: 기타)"),
                    fieldWithPath("currentLeaseDeposit").type(JsonFieldType.NUMBER)
                        .description("현재임차보증금")
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
                    fieldWithPath("data.loanAmount").type(JsonFieldType.NUMBER)
                        .description("대출금액"),
                    fieldWithPath("data.collateralValue").type(JsonFieldType.NUMBER)
                        .description("담보가치"),
                    fieldWithPath("data.priorMortgage").type(JsonFieldType.NUMBER)
                        .description("선순위채권"),
                    fieldWithPath("data.numbersOfRooms").type(JsonFieldType.NUMBER)
                        .description("방 수"),
                    fieldWithPath("data.smallAmountLeaseDeposit").type(JsonFieldType.NUMBER)
                        .description("소액임차보증금"),
                    fieldWithPath("data.topPriorityRepaymentAmount").type(JsonFieldType.NUMBER)
                        .description("최우선변제금"),
                    fieldWithPath("data.totalLoanExposure").type(JsonFieldType.NUMBER)
                        .description("총대출노출액"),
                    fieldWithPath("data.ltvRatio").type(JsonFieldType.NUMBER)
                        .description("LTV 비율")
                )
            ));
    }
}