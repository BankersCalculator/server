package com.myZipPlan.server.docs;


import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.advice.userInputInfo.controller.UserInputInfoApiController;
import com.myZipPlan.server.advice.userInputInfo.dto.UserInputInfoResponse;
import com.myZipPlan.server.advice.userInputInfo.dto.UserInputSummaryResponse;
import com.myZipPlan.server.advice.userInputInfo.service.UserInputInfoService;
import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.common.enums.loanAdvice.ChildStatus;
import com.myZipPlan.server.common.enums.loanAdvice.MaritalStatus;
import com.myZipPlan.server.housingInfo.buildingInfo.common.RentHousingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserInputInfoApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/userInputInfo";
    private final UserInputInfoService userInputInfoService = mock(UserInputInfoService.class);

    @Override
    protected Object initController() {
        return new UserInputInfoApiController(userInputInfoService);
    }


    @DisplayName("최근 제출된 유저 INPUT 조회")
    @Test
    void getRecentlySubmittedUserInput() throws Exception {
        UserInputInfoResponse response = createSampleUserInputInfoResponse();
        when(userInputInfoService.getRecentlySubmittedUserInput()).thenReturn(response);


        mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .header("AccessToken", "액세스 토큰")
            )
            .andExpect(status().isOk())
            .andDo(document("user-input-info/get-recently-submitted",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.rentalDeposit").type(JsonFieldType.NUMBER).description("임차보증금"),
                    fieldWithPath("data.monthlyRent").type(JsonFieldType.NUMBER).description("월세"),
                    fieldWithPath("data.cashOnHand").type(JsonFieldType.NUMBER).description("보유현금"),
                    fieldWithPath("data.age").type(JsonFieldType.NUMBER).description("만나이"),
                    fieldWithPath("data.maritalStatus").type(JsonFieldType.STRING).description("혼인상태(SINGLE /NEWLY_MARRIED / MARRIED / ENGAGED)"),
                    fieldWithPath("data.annualIncome").type(JsonFieldType.NUMBER).description("연소득"),
                    fieldWithPath("data.spouseAnnualIncome").type(JsonFieldType.NUMBER).description("배우자연소득"),
                    fieldWithPath("data.childStatus").type(JsonFieldType.STRING).description("자녀상태"),
                    fieldWithPath("data.hasNewborn").type(JsonFieldType.BOOLEAN).description("신생아여부"),
                    fieldWithPath("data.houseOwnershipType").type(JsonFieldType.STRING)
                        .description("주택 소유 형태 " +
                            "(LIFETIME_FIRST: 생애최초, " +
                            "ORDINARY_DEMAND: 서민실수요자, " +
                            "NO_HOUSE: 무주택, " +
                            "SINGLE_HOUSE: 1주택, " +
                            "MULTI_HOUSE: 다주택) "),
                    fieldWithPath("data.isSMEEmployee").type(JsonFieldType.BOOLEAN).description("중소기업재직여부"),
                    fieldWithPath("data.isNetAssetOver345M").type(JsonFieldType.BOOLEAN).description("순자산 3.45억 초과 여부"),
                    fieldWithPath("data.rentHousingType").type(JsonFieldType.STRING)
                        .description("주택 유형 " +
                            "(APARTMENT: 아파트, " +
                            "OFFICETEL: 오피스텔, " +
                            "HOUSEHOLD_HOUSE: 연립다세대, " +
                            "FAMILY_HOUSE: 단독/다가구, "),
                    fieldWithPath("data.exclusiveArea").type(JsonFieldType.NUMBER).description("전용면적"),
                    fieldWithPath("data.buildingName").type(JsonFieldType.STRING).description("건물명"),
                    fieldWithPath("data.districtCode").type(JsonFieldType.STRING).description("법정동 코드"),
                    fieldWithPath("data.dongName").type(JsonFieldType.STRING).description("읍명동이름"),
                    fieldWithPath("data.jibun").type(JsonFieldType.STRING).description("지번")
                )
            ));
    }

    @DisplayName("특정 유저 INPUT 조회")
    @Test
    void getSpecificUserInput() throws Exception {
        Long userInfoInputId = 1234L;
        UserInputInfoResponse response = createSampleUserInputInfoResponse();
        when(userInputInfoService.getSpecificUserInput(userInfoInputId)).thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/{userInfoInputId}", userInfoInputId)
                .accept(MediaType.APPLICATION_JSON)
                .header("AccessToken", "액세스 토큰"))
            .andExpect(status().isOk())
            .andDo(document("user-input-info/get-specific",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("userInfoInputId").description("조회할 유저 INPUT ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.rentalDeposit").type(JsonFieldType.NUMBER).description("임차보증금"),
                    fieldWithPath("data.monthlyRent").type(JsonFieldType.NUMBER).description("월세"),
                    fieldWithPath("data.cashOnHand").type(JsonFieldType.NUMBER).description("보유현금"),
                    fieldWithPath("data.age").type(JsonFieldType.NUMBER).description("만나이"),
                    fieldWithPath("data.maritalStatus").type(JsonFieldType.STRING).description("혼인상태(SINGLE /NEWLY_MARRIED / MARRIED / ENGAGED)"),
                    fieldWithPath("data.annualIncome").type(JsonFieldType.NUMBER).description("연소득"),
                    fieldWithPath("data.spouseAnnualIncome").type(JsonFieldType.NUMBER).description("배우자연소득"),
                    fieldWithPath("data.childStatus").type(JsonFieldType.STRING).description("자녀상태"),
                    fieldWithPath("data.hasNewborn").type(JsonFieldType.BOOLEAN).description("신생아여부"),
                    fieldWithPath("data.houseOwnershipType").type(JsonFieldType.STRING)
                        .description("주택 소유 형태 " +
                            "(LIFETIME_FIRST: 생애최초, " +
                            "ORDINARY_DEMAND: 서민실수요자, " +
                            "NO_HOUSE: 무주택, " +
                            "SINGLE_HOUSE: 1주택, " +
                            "MULTI_HOUSE: 다주택) "),
                    fieldWithPath("data.isSMEEmployee").type(JsonFieldType.BOOLEAN).description("중소기업재직여부"),
                    fieldWithPath("data.isNetAssetOver345M").type(JsonFieldType.BOOLEAN).description("순자산 3.45억 초과 여부"),
                    fieldWithPath("data.rentHousingType").type(JsonFieldType.STRING)
                        .description("주택 유형 " +
                            "(APARTMENT: 아파트, " +
                            "OFFICETEL: 오피스텔, " +
                            "HOUSEHOLD_HOUSE: 연립다세대, " +
                            "FAMILY_HOUSE: 단독/다가구, "),
                    fieldWithPath("data.exclusiveArea").type(JsonFieldType.NUMBER).description("전용면적"),
                    fieldWithPath("data.buildingName").type(JsonFieldType.STRING).description("건물명"),
                    fieldWithPath("data.districtCode").type(JsonFieldType.STRING).description("법정동 코드"),
                    fieldWithPath("data.dongName").type(JsonFieldType.STRING).description("읍명동이름"),
                    fieldWithPath("data.jibun").type(JsonFieldType.STRING).description("지번")
                )
            ));
    }

    @DisplayName("최근 5개의 유저 INPUT 요약 조회")
    @Test
    void getRecentUserInputs() throws Exception {
        List<UserInputSummaryResponse> summaries = Arrays.asList(
            createSampleUserInputSummary(),
            createSampleUserInputSummary()
        );
        when(userInputInfoService.getRecentUserInputs()).thenReturn(summaries);

        mockMvc.perform(get(BASE_URL + "/recent")
                .accept(MediaType.APPLICATION_JSON)
                .header("AccessToken", "액세스 토큰"))
            .andExpect(status().isOk())
            .andDo(document("user-input-info/get-recent-ten",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                    fieldWithPath("data[].userInputInfoId").type(JsonFieldType.NUMBER).description("UserInputInfo 고유ID"),
                    fieldWithPath("data[].inquiryDateTime").type(JsonFieldType.ARRAY).description("조회일시"),
                    fieldWithPath("data[].dongName").type(JsonFieldType.STRING).description("읍명동이름"),
                    fieldWithPath("data[].buildingName").type(JsonFieldType.STRING).description("건물명"),
                    fieldWithPath("data[].rentalDeposit").type(JsonFieldType.NUMBER).description("임차보증금"),
                    fieldWithPath("data[].monthlyRent").type(JsonFieldType.NUMBER).description("월세")
                )
            ));
    }

    private UserInputInfoResponse createSampleUserInputInfoResponse() {
        return UserInputInfoResponse.builder()
            .rentalDeposit(BigDecimal.valueOf(300000000))  // 3억원 임차보증금
            .monthlyRent(BigDecimal.valueOf(500000))       // 50만원 월세
            .cashOnHand(BigDecimal.valueOf(50000000))      // 5천만원 보유 현금
            .age(35)                    // 35세
            .maritalStatus(MaritalStatus.MARRIED)
            .annualIncome(BigDecimal.valueOf(60000000))    // 6천만원 연소득
            .spouseAnnualIncome(BigDecimal.valueOf(40000000))  // 4천만원 배우자 연소득
            .childStatus(ChildStatus.ONE_CHILD)
            .hasNewborn(true)
            .houseOwnershipType(HouseOwnershipType.SINGLE_HOUSE)
            .isSMEEmployee(true)        // 중소기업 재직 여부
            .isNetAssetOver345M(false)  // 순자산 3.45억 초과 여부
            .rentHousingType(RentHousingType.APARTMENT)
            .exclusiveArea(BigDecimal.valueOf(85.0))         // 85제곱미터 전용면적
            .buildingName("행복아파트")
            .districtCode("1168010100") // 서울특별시 강남구 삼성동
            .dongName("삼성동")
            .jibun("79-1")
            .build();
    }

    private UserInputSummaryResponse createSampleUserInputSummary() {
        return UserInputSummaryResponse.builder()
            .userInputInfoId(1234L)
            .inquiryDateTime(LocalDateTime.now())
            .dongName("삼성동")
            .buildingName("행복아파트")
            .rentalDeposit(BigDecimal.valueOf(300000000L))
            .monthlyRent(BigDecimal.valueOf(500000L))
            .build();
    }
}
