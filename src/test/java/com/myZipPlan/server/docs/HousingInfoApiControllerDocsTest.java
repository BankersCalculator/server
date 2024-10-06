package com.myZipPlan.server.docs;

import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.housingInfo.housingInfoMain.controller.HousingInfoApiController;
import com.myZipPlan.server.housingInfo.housingInfoMain.dto.HousingInfoResponse;
import com.myZipPlan.server.housingInfo.housingInfoMain.service.HousingInfoMainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class HousingInfoApiControllerDocsTest extends RestDocsSupport {

    private final HousingInfoMainService HousingInfoMainService = mock(HousingInfoMainService.class);

    @Override
    protected Object initController() {
        return new HousingInfoApiController(HousingInfoMainService);
    }

    @DisplayName("주택 정보 조회 API")
    @Test
    void getHousingInfo() throws Exception {

        // Mock the service response
        List<HousingInfoResponse> responseList = Arrays.asList(
                new HousingInfoResponse("오피스텔", 20.98, 6, 1000.0, 110.0, 2),
                new HousingInfoResponse("오피스텔", 20.52, 6, 1000.0, 107.5, 2)
        );

        // Mock the service to return the correct response format
        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("apiResultCode", "Y");
        serviceResponse.put("apiResultMessage", "Success");
        serviceResponse.put("housingInfoList", responseList);

        when(HousingInfoMainService.getHousingInfo(anyString(), anyString(), anyString())).thenReturn(serviceResponse);

        // Create request body
        String requestBody = "{\n" +
                "  \"districtCode\": \"1168010100\",\n" +
                "  \"jibun\": \"372-1\",\n" +
                "  \"dongName\": \"역삼동\"\n" +
                "}";

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/housingInfo")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("housing-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("districtCode").description("법정동 코드"),
                                fieldWithPath("jibun").description("지번 (예: 372-1)"),
                                fieldWithPath("dongName").description("읍/면/동 이름")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.apiResultCode").type(JsonFieldType.STRING).description("API 결과 코드"),
                                fieldWithPath("data.apiResultMessage").type(JsonFieldType.STRING).description("API 결과 메시지"),
                                fieldWithPath("data.housingInfoList").type(JsonFieldType.ARRAY).description("주택 정보 목록"),
                                fieldWithPath("data.housingInfoList[].rentHousingTypeName").type(JsonFieldType.STRING).description("임대 주택 유형 이름"),
                                fieldWithPath("data.housingInfoList[].exclusiveArea").type(JsonFieldType.NUMBER).description("전용 면적 (제곱미터)"),
                                fieldWithPath("data.housingInfoList[].exclusiveAreaPy").type(JsonFieldType.NUMBER).description("평수"),
                                fieldWithPath("data.housingInfoList[].averageDeposit").type(JsonFieldType.NUMBER).description("평균 보증금"),
                                fieldWithPath("data.housingInfoList[].averageMonthlyRent").type(JsonFieldType.NUMBER).description("평균 월세"),
                                fieldWithPath("data.housingInfoList[].transactionCount").type(JsonFieldType.NUMBER).description("거래 건수")
                        )
                ));
    }
}
