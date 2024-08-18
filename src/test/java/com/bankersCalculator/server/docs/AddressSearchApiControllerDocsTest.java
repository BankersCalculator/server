package com.bankersCalculator.server.docs;

import com.bankersCalculator.server.RestDocsSupport;
import com.bankersCalculator.server.housingInfo.addressSearch.controller.AddressSearchApiController;
import com.bankersCalculator.server.housingInfo.addressSearch.dto.AddressSearchRequest;
import com.bankersCalculator.server.housingInfo.addressSearch.service.AddressSearchService;
import com.bankersCalculator.server.housingInfo.addressSearch.dto.AddressSearchApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Collections;
import java.util.HashMap;
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

        Map<String, Object> response = new HashMap<>();
        response.put("errorCode", "0");
        response.put("errorMessage", "Success");
        response.put("addressList", Collections.singletonList(addressSearchApiResponse));

        when(addressSearchService.searchAddress(anyString())).thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                        .content("{\"keyword\": \"세종대로 110\"}")  // JSON 형식으로 요청 본문을 전달
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
                                fieldWithPath("errorCode").type(JsonFieldType.STRING).description("결과 코드"),
                                fieldWithPath("errorMessage").type(JsonFieldType.STRING).description("결과 메시지"),
                                fieldWithPath("addressList").type(JsonFieldType.ARRAY).description("주소정보"),
                                fieldWithPath("addressList[].roadAddress").type(JsonFieldType.STRING).description("전체 도로명 주소"),
                                fieldWithPath("addressList[].jibunAddress").type(JsonFieldType.STRING).description("전체 지번 주소"),
                                fieldWithPath("addressList[].buildingName").optional().type(JsonFieldType.STRING).description("건물명"),
                                fieldWithPath("addressList[].districtCode").type(JsonFieldType.STRING).description("행정구역 코드"),
                                fieldWithPath("addressList[].dongName").type(JsonFieldType.STRING).description("읍/면/동 이름"),
                                fieldWithPath("addressList[].jibun").type(JsonFieldType.STRING).description("지번(본번)-지번(부번)")
                        )
                ));
    }
}
