package com.myZipPlan.server.docs;

import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.housingInfo.addressSearch.controller.AddressSearchApiController;
import com.myZipPlan.server.housingInfo.addressSearch.dto.AddressSearchApiResponse;
import com.myZipPlan.server.housingInfo.addressSearch.dto.AddressSearchRequest;
import com.myZipPlan.server.housingInfo.addressSearch.service.AddressSearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AddressSearchApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/addressSearch";
    private final AddressSearchService addressSearchService = mock(AddressSearchService.class);

    @Override
    protected Object initController() {
        return new AddressSearchApiController(addressSearchService);
    }

    @DisplayName("주소 검색 API")
    @Test
    void searchAddress() throws Exception {
        // 요청 객체 설정
        AddressSearchRequest request = new AddressSearchRequest();
        request.setKeyword("청라한내로 100번길");

        // 응답 객체 설정
        AddressSearchApiResponse addressSearchApiResponse = new AddressSearchApiResponse();
        addressSearchApiResponse.setRoadAddress("인천광역시 서구 청라한내로100번길 10 (청라동)");
        addressSearchApiResponse.setJibunAddress("인천광역시 서구 청라동 95-1 청라 큐브시그니처 1차 오피스텔");
        addressSearchApiResponse.setBuildingName("청라 큐브시그니처 1차 오피스텔");
        addressSearchApiResponse.setDistrictCode("2826012200");
        addressSearchApiResponse.setDongName("청라동");
        addressSearchApiResponse.setJibun("95-1");

        // 응답을 LinkedHashMap으로 구성
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("apiResultCode", "0");
        response.put("apiResultMessage", "Success");
        response.put("addressInfos", Collections.singletonList(addressSearchApiResponse));

        when(addressSearchService.searchAddress(anyString())).thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                        .content("{\"keyword\": \"청라한내로 100번길\"}")  // JSON 형식으로 요청 본문을 전달
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("address/search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("keyword").type(JsonFieldType.STRING).description("주소 검색을 위한 키워드")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.apiResultCode").type(JsonFieldType.STRING).description("API 결과 코드"),
                                fieldWithPath("data.apiResultMessage").type(JsonFieldType.STRING).description("API 결과 메시지"),
                                fieldWithPath("data.addressInfos").type(JsonFieldType.ARRAY).description("주소 정보 목록"),
                                fieldWithPath("data.addressInfos[].roadAddress").type(JsonFieldType.STRING).description("전체 도로명 주소"),
                                fieldWithPath("data.addressInfos[].jibunAddress").type(JsonFieldType.STRING).description("전체 지번 주소"),
                                fieldWithPath("data.addressInfos[].buildingName").optional().type(JsonFieldType.STRING).description("건물명"),
                                fieldWithPath("data.addressInfos[].districtCode").type(JsonFieldType.STRING).description("행정구역 코드"),
                                fieldWithPath("data.addressInfos[].dongName").type(JsonFieldType.STRING).description("읍/면/동 이름"),
                                fieldWithPath("data.addressInfos[].jibun").type(JsonFieldType.STRING).description("지번(본번)-지번(부번)")
                        )
                ));
    }
}
