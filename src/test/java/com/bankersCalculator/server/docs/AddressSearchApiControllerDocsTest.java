package com.bankersCalculator.server.docs;

import com.bankersCalculator.server.RestDocsSupport;
import com.bankersCalculator.server.addressSearch.controller.AddressSearchApiController;
import com.bankersCalculator.server.addressSearch.service.AddressSearchService;
import com.bankersCalculator.server.addressSearch.dto.AddressSearchResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AddressSearchApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/addressSearch";
    private final AddressSearchService addressSearchService = mock(AddressSearchService.class);

    @Override
    protected Object initController() {
        return new AddressSearchApiController(addressSearchService);
    }

    @DisplayName("주소 검색 API")
    @Test
    void searchAddress() throws Exception {
        AddressSearchResponse addressSearchResponse = new AddressSearchResponse();
        addressSearchResponse.setRoadAddr("서울특별시 중구 세종대로 110");
        addressSearchResponse.setJibunAddr("서울특별시 중구 태평로1가 31");
        addressSearchResponse.setZipNo("04524");
        addressSearchResponse.setAdmCd("1114010200");

        when(addressSearchService.searchAddress(anyString()))
            .thenReturn(Collections.singletonMap("jusoList", Collections.singletonList(addressSearchResponse)));


        mockMvc.perform(get(BASE_URL)
                .param("keyword", "세종대로 110")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("address/search",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("errorCode").type(JsonFieldType.STRING).description("결과 코드"),
                    fieldWithPath("errorMessage").type(JsonFieldType.STRING).description("결과 메시지"),
                    fieldWithPath("jusoList").type(JsonFieldType.ARRAY).description("주소 목록"),
                    fieldWithPath("jusoList[].roadAddr").type(JsonFieldType.STRING).description("도로명 주소"),
                    fieldWithPath("jusoList[].jibunAddr").type(JsonFieldType.STRING).description("지번 주소"),
                    fieldWithPath("jusoList[].zipNo").type(JsonFieldType.STRING).description("우편번호"),
                    fieldWithPath("jusoList[].admCd").type(JsonFieldType.STRING).description("행정구역 코드"),
                    fieldWithPath("jusoList[].rnMgtSn").optional().type(JsonFieldType.STRING).description("도로명 관리 번호"),
                    fieldWithPath("jusoList[].bdMgtSn").optional().type(JsonFieldType.STRING).description("건물 관리 번호"),
                    fieldWithPath("jusoList[].detBdNmList").optional().type(JsonFieldType.STRING).description("상세 건물명 리스트"),
                    fieldWithPath("jusoList[].bdNm").optional().type(JsonFieldType.STRING).description("건물명"),
                    fieldWithPath("jusoList[].bdKdcd").optional().type(JsonFieldType.STRING).description("건물 군 코드"),
                    fieldWithPath("jusoList[].siNm").optional().type(JsonFieldType.STRING).description("시/도 이름"),
                    fieldWithPath("jusoList[].sggNm").optional().type(JsonFieldType.STRING).description("시/군/구 이름"),
                    fieldWithPath("jusoList[].emdNm").optional().type(JsonFieldType.STRING).description("읍/면/동 이름"),
                    fieldWithPath("jusoList[].liNm").optional().type(JsonFieldType.STRING).description("리 이름"),
                    fieldWithPath("jusoList[].rn").optional().type(JsonFieldType.STRING).description("도로명"),
                    fieldWithPath("jusoList[].udrtYn").optional().type(JsonFieldType.STRING).description("지하 여부 (0: 지상, 1: 지하)"),
                    fieldWithPath("jusoList[].buldMnnm").optional().type(JsonFieldType.NUMBER).description("건물 본번"),
                    fieldWithPath("jusoList[].buldSlno").optional().type(JsonFieldType.NUMBER).description("건물 부번"),
                    fieldWithPath("jusoList[].mtYn").optional().type(JsonFieldType.STRING).description("산 여부 (0: 일반, 1: 산)"),
                    fieldWithPath("jusoList[].lnbrMnnm").optional().type(JsonFieldType.NUMBER).description("지번 본번"),
                    fieldWithPath("jusoList[].lnbrSlno").optional().type(JsonFieldType.NUMBER).description("지번 부번"),
                    fieldWithPath("jusoList[].emdNo").optional().type(JsonFieldType.STRING).description("행정동 코드")
                )
            ));
    }
}
