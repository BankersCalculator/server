package com.bankersCalculator.server.docs;

import com.bankersCalculator.server.RestDocsSupport;
import com.bankersCalculator.server.advice.userInputInfo.controller.UserInputInfoApiController;
import com.bankersCalculator.server.advice.userInputInfo.dto.UserInputInfoResponse;
import com.bankersCalculator.server.advice.userInputInfo.dto.UserInputSummaryResponse;
import com.bankersCalculator.server.advice.userInputInfo.service.UserInputInfoService;
import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
                .header("RefreshToken", "리프레시 토큰")
            )
            .andExpect(status().isOk())
            .andDo(document("user-input-info/get-recently-submitted",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰"),
                    headerWithName("RefreshToken")
                        .description("리프레쉬 토큰")
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

        mockMvc.perform(get(BASE_URL + "/specific/{userInfoInputId}", userInfoInputId)
                .accept(MediaType.APPLICATION_JSON)
                .header("AccessToken", "액세스 토큰")
                .header("RefreshToken", "리프레시 토큰"))
            .andExpect(status().isOk())
            .andDo(document("user-input-info/get-specific",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰"),
                    headerWithName("RefreshToken")
                        .description("리프레쉬 토큰")
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

    @DisplayName("최근 10개의 유저 INPUT 요약 조회")
    @Test
    void getRecentUserInputs() throws Exception {
        List<UserInputSummaryResponse> summaries = Arrays.asList(
            createSampleUserInputSummary(),
            createSampleUserInputSummary()
        );
        when(userInputInfoService.getRecentUserInputs()).thenReturn(summaries);

        mockMvc.perform(get(BASE_URL + "/recent-ten")
                .accept(MediaType.APPLICATION_JSON)
                .header("AccessToken", "액세스 토큰")
                .header("RefreshToken", "리프레시 토큰"))
            .andExpect(status().isOk())
            .andDo(document("user-input-info/get-recent-ten",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰"),
                    headerWithName("RefreshToken")
                        .description("리프레쉬 토큰")
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
            .rentalDeposit(300000000L)
            .monthlyRent(500000L)
            .cashOnHand(50000000L)
            .age(35)
            .maritalStatus(MaritalStatus.MARRIED)
            .annualIncome(60000000L)
            .spouseAnnualIncome(40000000L)
            .childStatus(ChildStatus.ONE_CHILD)
            .hasNewborn(true)
            .isSMEEmployee(true)
            .isNetAssetOver345M(false)
            .rentHousingType(RentHousingType.APARTMENT)
            .exclusiveArea(85L)
            .buildingName("행복아파트")
            .districtCode("1168010100")
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
            .rentalDeposit(300000000L)
            .monthlyRent(500000L)
            .build();
    }
}
