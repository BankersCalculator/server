package com.bankersCalculator.server.docs;

import com.bankersCalculator.server.RestDocsSupport;
import com.bankersCalculator.server.advise.loanAdvise.controller.LoanAdviseApiController;
import com.bankersCalculator.server.advise.loanAdvise.dto.*;
import com.bankersCalculator.server.advise.userInputInfo.dto.UserInputInfoRequest;
import com.bankersCalculator.server.advise.userInputInfo.dto.UserInputInfoResponse;
import com.bankersCalculator.server.advise.loanAdvise.service.LoanAdviseService;
import com.bankersCalculator.server.common.enums.Bank;
import com.bankersCalculator.server.common.enums.loanAdvise.AreaSize;
import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.RentalType;
import com.bankersCalculator.server.common.enums.ltv.HousingType;
import com.bankersCalculator.server.common.enums.ltv.RegionType;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.util.Arrays;

import static com.bankersCalculator.server.common.enums.loanAdvise.UserType.MEMBER;
import static com.bankersCalculator.server.common.enums.loanAdvise.UserType.NON_MEMBER;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
            .rentalDeposit(300000000L)  // 3억원 임차보증금
            .monthlyRent(500000L)       // 50만원 월세
            .cashOnHand(50000000L)      // 5천만원 보유 현금
            .age(35)                    // 35세
            .maritalStatus(MaritalStatus.MARRIED)
            .annualIncome(60000000L)    // 6천만원 연소득
            .spouseAnnualIncome(40000000L)  // 4천만원 배우자 연소득
            .childStatus(ChildStatus.ONE_CHILD)
            .hasNewborn(true)
            .isSMEEmployee(true)        // 중소기업 재직 여부
            .isNetAssetOver345M(false)  // 순자산 3.45억 초과 여부
            .rentHousingType(RentHousingType.APARTMENT)
            .exclusiveArea(85L)         // 85제곱미터 전용면적
            .buildingName("행복아파트")
            .districtCode("1168010100") // 서울특별시 강남구 삼성동
            .dongName("삼성동")
            .jibun("79-1")
            .build();

        when(loanAdviseService.getSubmittedUserInput(any())).thenReturn(response);


        mockMvc.perform(get(BASE_URL + "/userInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .header("accessToken", "액세스 토큰")
                .header("refreshToken", "리프레시 토큰")
            )
            .andExpect(status().isOk())
            .andDo(document("loan-advise/get-submitted-user-input",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("accessToken")
                        .description("액세스 토큰"),
                    headerWithName("refreshToken")
                        .description("리프레쉬 토큰")
                ),
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
                        .description("지역 유형 (SEOUL: 서울 / CAPITAL_AREA: 수도권 / METROPOLITAN_CITY: 광역시 / OTHER_AREAS: 기타"),
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
                    fieldWithPath("data.housingPrice").type(JsonFieldType.NUMBER)
                        .description("주택 가격"),
                    fieldWithPath("data.priorDepositAndClaims").type(JsonFieldType.NUMBER)
                        .description("선순위 보증금 및 채권"),
                    fieldWithPath("data.isNetAssetOver345M").type(JsonFieldType.BOOLEAN)
                        .description("순자산 3억4500만원 초과 여부")
                )
            ));
    }

    @DisplayName("대출 상담 결과 생성")
    @Test
    void generateLoanAdvise() throws Exception {
        LoanAdviseRequest request = createSampleLoanAdviseRequest();
        LoanAdviseResponse response = createSampleLoanAdviseResponse();

        when(loanAdviseService.generateLoanAdvise(any())).thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("accessToken", "액세스 토큰")
                .header("refreshToken", "리프레시 토큰")
                .header("tempUserId", "일회성 유저 ID")
            )
            .andExpect(status().isOk())
            .andDo(document("loan-advise/generate-loan-advise",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("accessToken")
                        .description("액세스 토큰"),
                    headerWithName("refreshToken")
                        .description("리프레쉬 토큰"),
                    headerWithName("tempUserId")
                        .description("일회성 유저 ID")
                ),
                requestFields(
                    fieldWithPath("age").type(JsonFieldType.NUMBER).description("만나이"),
                    fieldWithPath("annualIncome").type(JsonFieldType.NUMBER).description("연소득"),
                    fieldWithPath("maritalStatus").type(JsonFieldType.STRING).description("혼인상태 (SINGLE, MARRIED, ENGAGED)"),
                    fieldWithPath("newlyWedding").type(JsonFieldType.BOOLEAN).description("신혼여부"),
                    fieldWithPath("weddingDate").type(JsonFieldType.ARRAY).description("혼인(예정)일"),
                    fieldWithPath("spouseAnnualIncome").type(JsonFieldType.NUMBER).description("배우자연소득"),
                    fieldWithPath("cashOnHand").type(JsonFieldType.NUMBER).description("보유현금"),
                    fieldWithPath("childStatus").type(JsonFieldType.STRING).description("자녀상태 (NO_CHILD, ONE_CHILD, TWO_CHILD, THREE_OR_MORE_CHILDREN)"),
                    fieldWithPath("hasNewborn").type(JsonFieldType.BOOLEAN).description("신생아여부"),
                    fieldWithPath("worksForSME").type(JsonFieldType.BOOLEAN).description("중소기업재직여부"),
                    fieldWithPath("housingType").type(JsonFieldType.STRING)
                        .description("주택 유형 " +
                        "(APARTMENT: 아파트, " +
                        "DETACHED_HOUSE: 단독주택, " +
                        "MULTI_FAMILY_HOUSE: 다가구주택, " +
                        "MULTI_HOUSEHOLD_HOUSE: 다세대주택, " +
                        "OFFICETEL: 오피스텔, " +
                        "OTHER: 기타)"),
                    fieldWithPath("rentalArea").type(JsonFieldType.STRING).description("임차전용면적 (UNDER_85_SQM, OVER_85_SQM)"),
                    fieldWithPath("regionType").type(JsonFieldType.STRING)
                        .description("지역 유형 (SEOUL: 서울 / CAPITAL_AREA: 수도권 / METROPOLITAN_CITY: 광역시 / OTHER_AREAS: 기타"),
                    fieldWithPath("propertyName").type(JsonFieldType.STRING).description("건물명"),
                    fieldWithPath("individualRentalArea").type(JsonFieldType.NUMBER).description("임차전용면적"),
                    fieldWithPath("rentalCostList").type(JsonFieldType.ARRAY).description("임차비용 목록"),
                    fieldWithPath("rentalCostList[].rentalType").type(JsonFieldType.STRING).description("임대 유형 (JEONSE, BANJEONSE, WOLSE)"),
                    fieldWithPath("rentalCostList[].rentalDeposit").type(JsonFieldType.NUMBER).description("임대 보증금"),
                    fieldWithPath("rentalCostList[].monthlyRent").type(JsonFieldType.NUMBER).description("월 임대료"),
                    fieldWithPath("housingPrice").type(JsonFieldType.NUMBER).description("주택가액"),
                    fieldWithPath("priorDepositAndClaims").type(JsonFieldType.NUMBER).description("선순위임차보증금 and 선순위채권"),
                    fieldWithPath("isNetAssetOver345M").type(JsonFieldType.BOOLEAN).description("순자산 3.45억 초과 여부")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.loanAdviseResultId").type(JsonFieldType.NUMBER).description("대출 상담 결과 ID"),
                    fieldWithPath("data.loanProductName").type(JsonFieldType.STRING).description("대출 상품명"),
                    fieldWithPath("data.loanProductCode").type(JsonFieldType.STRING).description("대출 상품 코드"),
                    fieldWithPath("data.possibleLoanLimit").type(JsonFieldType.NUMBER).description("가능한 대출 한도"),
                    fieldWithPath("data.expectedLoanRate").type(JsonFieldType.NUMBER).description("예상 대출 금리"),
                    fieldWithPath("data.totalRentalDeposit").type(JsonFieldType.NUMBER).description("총 임대 보증금"),
                    fieldWithPath("data.loanAmount").type(JsonFieldType.NUMBER).description("대출 금액"),
                    fieldWithPath("data.ownFunds").type(JsonFieldType.NUMBER).description("자기 자금"),
                    fieldWithPath("data.monthlyInterestCost").type(JsonFieldType.NUMBER).description("월 이자 비용"),
                    fieldWithPath("data.monthlyRent").type(JsonFieldType.NUMBER).description("월 임대료"),
                    fieldWithPath("data.totalLivingCost").type(JsonFieldType.NUMBER).description("총 주거 비용"),
                    fieldWithPath("data.opportunityCostOwnFunds").type(JsonFieldType.NUMBER).description("자기 자금 기회 비용"),
                    fieldWithPath("data.depositInterestRate").type(JsonFieldType.NUMBER).description("예금 이자율"),
                    fieldWithPath("data.calculatedCost").type(JsonFieldType.NUMBER).description("계산된 비용"),
                    fieldWithPath("data.guaranteeInsuranceFee").type(JsonFieldType.NUMBER).description("보증 보험료"),
                    fieldWithPath("data.stampDuty").type(JsonFieldType.NUMBER).description("인지세"),
                    fieldWithPath("data.recommendationReason").type(JsonFieldType.STRING).description("추천 이유"),
                    fieldWithPath("data.recommendedProducts").type(JsonFieldType.ARRAY).description("추천 상품 목록"),
                    fieldWithPath("data.recommendedProducts[].rank").type(JsonFieldType.NUMBER).description("추천 순위"),
                    fieldWithPath("data.recommendedProducts[].loanProductName").type(JsonFieldType.STRING).description("대출 상품명"),
                    fieldWithPath("data.recommendedProducts[].loanProductCode").type(JsonFieldType.STRING).description("대출 상품 코드"),
                    fieldWithPath("data.recommendedProducts[].possibleLoanLimit").type(JsonFieldType.NUMBER).description("가능한 대출 한도"),
                    fieldWithPath("data.recommendedProducts[].expectedLoanRate").type(JsonFieldType.NUMBER).description("예상 대출 금리"),
                    fieldWithPath("data.recommendedProducts[].notEligibleReason").type(JsonFieldType.STRING).optional().description("부적격 사유"),
                    fieldWithPath("data.availableBanks").type(JsonFieldType.ARRAY).description("이용 가능한 은행 목록"),
                    fieldWithPath("data.rentalLoanGuide").type(JsonFieldType.STRING).description("전세 대출 가이드")
                )
            ));
    }

    @DisplayName("특정 대출 상품에 대한 대출 상담 결과 생성")
    @Test
    void generateLoanAdviseOnSpecificLoan() throws Exception {
        String productCode = "SAMPLE001";
        Long userId = 1L;
        Long adviseResultId = 1L;
        LoanAdviseResponse response = createSampleLoanAdviseResponse();

        SpecificLoanAdviseRequest request = SpecificLoanAdviseRequest.builder().userId(1L).userType(MEMBER).adviseResultId(200L).build();

        when(loanAdviseService.generateLoanAdviseOnSpecificLoan(anyString(), anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/{productCode}", productCode)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("accessToken", "액세스 토큰")
                .header("refreshToken", "리프레시 토큰")
            )
            .andExpect(status().isOk())
            .andDo(document("loan-advise/generate-loan-advise-on-specific-loan",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("accessToken")
                        .description("액세스 토큰"),
                    headerWithName("refreshToken")
                        .description("리프레쉬 토큰")
                ),
                pathParameters(
                    parameterWithName("productCode").description("대출 상품 코드")
                ),
                requestFields(
                    fieldWithPath("userId").type(JsonFieldType.NUMBER)
                        .description("사용자 ID"),
                    fieldWithPath("userType").type(JsonFieldType.STRING)
                        .description("유저 타입(MEMBER: 회원 / NON_MEMBER: 비회원)"),
                    fieldWithPath("adviseResultId").type(JsonFieldType.NUMBER)
                        .description("기존 대출 상담 결과 ID")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.loanAdviseResultId").type(JsonFieldType.NUMBER).description("대출 상담 결과 ID"),
                    fieldWithPath("data.loanProductName").type(JsonFieldType.STRING).description("대출 상품명"),
                    fieldWithPath("data.loanProductCode").type(JsonFieldType.STRING).description("대출 상품 코드"),
                    fieldWithPath("data.possibleLoanLimit").type(JsonFieldType.NUMBER).description("가능한 대출 한도"),
                    fieldWithPath("data.expectedLoanRate").type(JsonFieldType.NUMBER).description("예상 대출 금리"),
                    fieldWithPath("data.totalRentalDeposit").type(JsonFieldType.NUMBER).description("총 임대 보증금"),
                    fieldWithPath("data.loanAmount").type(JsonFieldType.NUMBER).description("대출 금액"),
                    fieldWithPath("data.ownFunds").type(JsonFieldType.NUMBER).description("자기 자금"),
                    fieldWithPath("data.monthlyInterestCost").type(JsonFieldType.NUMBER).description("월 이자 비용"),
                    fieldWithPath("data.monthlyRent").type(JsonFieldType.NUMBER).description("월 임대료"),
                    fieldWithPath("data.totalLivingCost").type(JsonFieldType.NUMBER).description("총 주거 비용"),
                    fieldWithPath("data.opportunityCostOwnFunds").type(JsonFieldType.NUMBER).description("자기 자금 기회 비용"),
                    fieldWithPath("data.depositInterestRate").type(JsonFieldType.NUMBER).description("예금 이자율"),
                    fieldWithPath("data.calculatedCost").type(JsonFieldType.NUMBER).description("계산된 비용"),
                    fieldWithPath("data.guaranteeInsuranceFee").type(JsonFieldType.NUMBER).description("보증 보험료"),
                    fieldWithPath("data.stampDuty").type(JsonFieldType.NUMBER).description("인지세"),
                    fieldWithPath("data.recommendationReason").type(JsonFieldType.STRING).description("추천 이유"),
                    fieldWithPath("data.recommendedProducts").type(JsonFieldType.ARRAY).description("추천 상품 목록"),
                    fieldWithPath("data.recommendedProducts[].rank").type(JsonFieldType.NUMBER).description("추천 순위"),
                    fieldWithPath("data.recommendedProducts[].loanProductName").type(JsonFieldType.STRING).description("대출 상품명"),
                    fieldWithPath("data.recommendedProducts[].loanProductCode").type(JsonFieldType.STRING).description("대출 상품 코드"),
                    fieldWithPath("data.recommendedProducts[].possibleLoanLimit").type(JsonFieldType.NUMBER).description("가능한 대출 한도"),
                    fieldWithPath("data.recommendedProducts[].expectedLoanRate").type(JsonFieldType.NUMBER).description("예상 대출 금리"),
                    fieldWithPath("data.recommendedProducts[].notEligibleReason").type(JsonFieldType.STRING).optional().description("부적격 사유 (해당되는 경우)"),
                    fieldWithPath("data.availableBanks").type(JsonFieldType.ARRAY).description("이용 가능한 은행 목록"),
                    fieldWithPath("data.rentalLoanGuide").type(JsonFieldType.STRING).description("전세 대출 가이드")
                )
            ));
    }


    private LoanAdviseRequest createSampleLoanAdviseRequest() {
        return LoanAdviseRequest.builder()
            .rentalDeposit(200000000L)  // 2억원 임차보증금
            .monthlyRent(0L)            // 전세이므로 월세 0원
            .cashOnHand(20000000L)      // 2천만원 보유현금
            .age(30)                    // 30세
            .maritalStatus(MaritalStatus.MARRIED)
            .annualIncome(50000000L)    // 5천만원 연소득
            .spouseAnnualIncome(40000000L)  // 4천만원 배우자 연소득
            .childStatus(ChildStatus.ONE_CHILD)
            .hasNewborn(true)
            .isSMEEmployee(false)       // 중소기업 재직 아님
            .isNetAssetOver345M(false)  // 순자산 3.45억 초과 아님
            .rentHousingType(RentHousingType.APARTMENT)
            .exclusiveArea(75L)         // 75제곱미터 전용면적
            .buildingName("Sample Apartment")
            .districtCode("1168010100") // 예: 서울특별시 강남구 삼성동
            .dongName("삼성동")
            .jibun("79-1")
            .build();
    }

    private LoanAdviseResponse createSampleLoanAdviseResponse() {
        return LoanAdviseResponse.builder()
            .loanAdviseResultId(1L)
            .loanProductName("샘플 전세자금대출")
            .loanProductCode("SAMPLE001")
            .possibleLoanLimit(200000000.0)
            .expectedLoanRate(3.5)
            .totalRentalDeposit(300000000L)
            .loanAmount(200000000L)
            .ownFunds(100000000L)
            .monthlyInterestCost(583333L)
            .monthlyRent(0L)
            .totalLivingCost(583333L)
            .opportunityCostOwnFunds(100000000L)
            .depositInterestRate(2.5)
            .calculatedCost(2500000L)
            .guaranteeInsuranceFee(1000000L)
            .stampDuty(150000L)
            .recommendationReason("고객님의 소득과 신용도를 고려하여 가장 적합한 상품으로 선정되었습니다.")
            .recommendedProducts(Arrays.asList(
                RecommendedProductDto.builder()
                    .rank(2)
                    .loanProductName("다른 은행 전세자금대출")
                    .loanProductCode("OTHER001")
                    .possibleLoanLimit(180000000.0)
                    .expectedLoanRate(3.7)
                    .notEligibleReason(null)
                    .build(),
                RecommendedProductDto.builder()
                    .rank(3)
                    .loanProductName("보증금 반환 보증 전세자금대출")
                    .loanProductCode("GUARANTEE001")
                    .possibleLoanLimit(220000000.0)
                    .expectedLoanRate(3.8)
                    .notEligibleReason("보증금 반환 보증 가입 필요")
                    .build()
            ))
            .availableBanks(Arrays.asList(Bank.KOOMIN, Bank.SHINHAN, Bank.WOORI))
            .rentalLoanGuide("전세자금대출 이용 시 주의사항:\n1. 대출 기간 동안 이자를 꾸준히 납부해야 합니다.\n2. 전세 계약 만료 시 대출금 상환 계획을 미리 세워야 합니다.")
            .build();
    }
}
