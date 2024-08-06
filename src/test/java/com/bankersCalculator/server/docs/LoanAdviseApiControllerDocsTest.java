package com.bankersCalculator.server.docs;

import com.bankersCalculator.server.RestDocsSupport;
import com.bankersCalculator.server.advise.loanAdvise.controller.LoanAdviseApiController;
import com.bankersCalculator.server.advise.loanAdvise.domain.RentalCost;
import com.bankersCalculator.server.advise.loanAdvise.dto.RentalCostDto;
import com.bankersCalculator.server.advise.loanAdvise.dto.UserInputInfoRequest;
import com.bankersCalculator.server.advise.loanAdvise.dto.UserInputInfoResponse;
import com.bankersCalculator.server.advise.loanAdvise.service.LoanAdviseService;
import com.bankersCalculator.server.calculator.repaymentCalc.controller.RepaymentCalcApiController;
import com.bankersCalculator.server.calculator.repaymentCalc.domain.RepaymentSchedule;
import com.bankersCalculator.server.calculator.repaymentCalc.dto.RepaymentCalcRequest;
import com.bankersCalculator.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.bankersCalculator.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.bankersCalculator.server.common.enums.RepaymentType;
import com.bankersCalculator.server.common.enums.loanAdvise.AreaSize;
import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.UserType;
import com.bankersCalculator.server.common.enums.ltv.HousingType;
import com.bankersCalculator.server.common.enums.ltv.RegionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.util.Arrays;

import static com.bankersCalculator.server.common.enums.loanAdvise.UserType.NON_MEMBER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoanAdviseApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/loanAdvise";
    private final LoanAdviseService loanAdviseService = mock(LoanAdviseService.class);

    @Override
    protected Object initController() {
        return new LoanAdviseApiController(loanAdviseService);
    }


    @DisplayName("투입된 유저 INPUT 재조회")
    @Test
    void getSubmittedUserInput() throws Exception {

        UserInputInfoRequest request = UserInputInfoRequest.builder()
            .userId("A00001")
            .userType(NON_MEMBER)
            .build();

        UserInputInfoResponse response = UserInputInfoResponse.builder()
            .age(30)
            .annualIncome(50000000L)
            .maritalStatus(MaritalStatus.MARRIED)
            .newlyWedding(true)
            .weddingDate(LocalDate.of(2023, 1, 1))
            .spouseAnnualIncome(40000000L)
            .cashOnHand(20000000L)
            .childStatus(ChildStatus.ONE_CHILD)
            .hasNewborn(true)
            .worksForSME(false)
            .housingType(HousingType.APARTMENT)
            .rentalArea(AreaSize.UNDER_85_SQM)
            .regionType(RegionType.SEOUL)
            .propertyName("Sample Apartment")
            .manualInputRentalArea(75L)
            .rentalCostList(Arrays.asList(new RentalCostDto()))
            .housingPrice(300000000L)
            .priorDepositAndClaims(50000000L)
            .isNetAssetOver345M(false)
            .build();

        when(loanAdviseService.getSubmittedUserInput(any())).thenReturn(response);


        mockMvc.perform(get(BASE_URL + "/userInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andDo(document("loan-advise/get-submitted-user-input",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("userType").type(JsonFieldType.STRING)
                        .description("유저 타입(MEMBER: 회원 / NON_MEMBER: 비회원)"),
                    fieldWithPath("userId").type(JsonFieldType.STRING)
                        .description("사용자 ID")
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
                    fieldWithPath("data.age").type(JsonFieldType.NUMBER)
                        .description("만나이"),
                    fieldWithPath("data.annualIncome").type(JsonFieldType.NUMBER)
                        .description("연소득"),
                    fieldWithPath("data.maritalStatus").type(JsonFieldType.STRING)
                        .description("결혼 상태 (SINGLE / MARRIED / ENGAGED)"),
                    fieldWithPath("data.newlyWedding").type(JsonFieldType.BOOLEAN)
                        .description("신혼 여부"),
                    fieldWithPath("data.weddingDate").type(JsonFieldType.ARRAY)
                        .description("혼인(예정)일"),
                    fieldWithPath("data.spouseAnnualIncome").type(JsonFieldType.NUMBER)
                        .description("배우자 연소득"),
                    fieldWithPath("data.cashOnHand").type(JsonFieldType.NUMBER)
                        .description("보유 현금"),
                    fieldWithPath("data.childStatus").type(JsonFieldType.STRING)
                        .description("자녀 상태 (NO_CHILD / ONE_CHILD / TWO_CHILD / THREE_OR_MORE_CHILDREN"),
                    fieldWithPath("data.hasNewborn").type(JsonFieldType.BOOLEAN)
                        .description("신생아 여부"),
                    fieldWithPath("data.worksForSME").type(JsonFieldType.BOOLEAN)
                        .description("중소기업 근무 여부"),
                    fieldWithPath("data.housingType").type(JsonFieldType.STRING)
                        .description("주택 유형 " +
                            "(APARTMENT: 아파트, " +
                            "DETACHED_HOUSE: 단독주택, " +
                            "MULTI_FAMILY_HOUSE: 다가구주택, " +
                            "MULTI_HOUSEHOLD_HOUSE: 다세대주택, " +
                            "OFFICETEL: 오피스텔, " +
                            "OTHER: 기타)"),
                    fieldWithPath("data.rentalArea").type(JsonFieldType.STRING)
                        .description("임대 면적 크기 (UNDER_85_SQM: 85제곱이하 / OVER_85_SQM: 85제곱초과"),
                    fieldWithPath("data.regionType").type(JsonFieldType.STRING)
                        .description("지역 유형 (SEOUL: 서울 / CAPITAL_AREA: 수도권 / METROPOLITAN_CITY: 광역시 / OTHER_AREAS: 기"),
                    fieldWithPath("data.propertyName").type(JsonFieldType.STRING)
                        .description("부동산 이름"),
                    fieldWithPath("data.manualInputRentalArea").type(JsonFieldType.NUMBER)
                        .description("수기투입 임차전용면적"),
                    fieldWithPath("data.rentalCostList").type(JsonFieldType.ARRAY)
                        .description("임대 비용 목록"),
                    fieldWithPath("data.rentalCostList[].id").type(JsonFieldType.NUMBER)
                        .description("임대 비용 ID")
                        .optional(),
                    fieldWithPath("data.rentalCostList[].rentalType").type(JsonFieldType.STRING)
                        .description("임대 유형(JEONSE: 전세 / BANJEONSE: 반전세 / WOLSE: 월세 ")
                        .optional(),
                    fieldWithPath("data.rentalCostList[].rentalDeposit").type(JsonFieldType.NUMBER)
                        .description("임대 보증금"),
                    fieldWithPath("data.rentalCostList[].monthlyRent").type(JsonFieldType.NUMBER)
                        .description("월 임대료"),
                    fieldWithPath("data.rentalCostList[].userInputInfo").type(JsonFieldType.OBJECT)
                        .description("내부 참조용 필드 (API 응답에서 무시할 것)").optional(),
                    fieldWithPath("data.housingPrice").type(JsonFieldType.NUMBER)
                        .description("주택 가격"),
                    fieldWithPath("data.priorDepositAndClaims").type(JsonFieldType.NUMBER)
                        .description("선순위 보증금 및 채권"),
                    fieldWithPath("data.isNetAssetOver345M").type(JsonFieldType.BOOLEAN)
                        .description("순자산 3억4500만원 초과 여부")
                )
            ));
    }
}
