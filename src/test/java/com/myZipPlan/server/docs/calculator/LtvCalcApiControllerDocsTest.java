package com.myZipPlan.server.docs.calculator;

import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.calculator.ltvCalc.controller.LtvCalcApiController;
import com.myZipPlan.server.calculator.ltvCalc.dto.LtvCalcRequest;
import com.myZipPlan.server.calculator.ltvCalc.dto.LtvCalcResponse;
import com.myZipPlan.server.calculator.ltvCalc.service.LtvCalcService;
import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.common.enums.calculator.HousingType;
import com.myZipPlan.server.common.enums.calculator.LoanPurpose;
import com.myZipPlan.server.common.enums.calculator.RegionType;
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

public class LtvCalcApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/ltvCalc";
    private final LtvCalcService ltvCalcService = mock(LtvCalcService.class);

    @Override
    protected Object initController() {
        return new LtvCalcApiController(ltvCalcService);
    }

    @DisplayName("LTV 계산기 API")
    @Test
    void calculateLtv() throws Exception {
        LtvCalcRequest request = LtvCalcRequest.builder()
            .loanPurpose(LoanPurpose.HOME_PURCHASE)
            .collateralValue(BigDecimal.valueOf(500000000))
            .regionType(RegionType.REGULATED_AREA)
            .houseOwnershipType(HouseOwnershipType.NO_HOUSE)
            .build();

        LtvCalcResponse response = LtvCalcResponse.builder()
                .ltvRatio(BigDecimal.valueOf(0.6))
                .collateralValue(BigDecimal.valueOf(500000000))
                .possibleLoanAmount(BigDecimal.valueOf(300000000))
                .build();

        when(ltvCalcService.calculate(any()))
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
                    fieldWithPath("loanPurpose").type(JsonFieldType.STRING)
                        .description("대출 목적 (HOME_PURCHASE: 주택구입자금, LIVING_STABILITY: 생활안정자금)"),
                    fieldWithPath("collateralValue").type(JsonFieldType.NUMBER)
                        .description("담보가치"),
                    fieldWithPath("regionType").type(JsonFieldType.STRING)
                        .description("지역 유형 (REGULATED_AREA: 규제지역, NON_REGULATED_CAPITAL_AREA: 규제지역 외 수도권, OTHER_AREAS: 기타)"),
                    fieldWithPath("houseOwnershipType").type(JsonFieldType.STRING)
                        .description("주택 소유 유형\n" +
                            "주택구입자금:\n" +
                            "  LIFETIME_FIRST: 생애최초\n" +
                            "  ORDINARY_DEMAND: 서민실수요자\n" +
                            "  NO_HOUSE: 무주택\n" +
                            "  SINGLE_HOUSE_DISPOSAL: 1주택 처분 조건\n" +
                            "  MORE_THAN_ONE_HOUSE: 1주택 이상\n" +
                            "생활안정자금:\n" +
                            "  SINGLE_HOUSE: 1주택\n" +
                            "  MORE_THAN_TWO_HOUSE: 2주택 이상")
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
                    fieldWithPath("data.ltvRatio").type(JsonFieldType.NUMBER)
                        .description("LTV 비율"),
                    fieldWithPath("data.collateralValue").type(JsonFieldType.NUMBER)
                        .description("담보가치"),
                    fieldWithPath("data.possibleLoanAmount").type(JsonFieldType.NUMBER)
                        .description("가능한 대출 금액")
                )
            ));
    }
}