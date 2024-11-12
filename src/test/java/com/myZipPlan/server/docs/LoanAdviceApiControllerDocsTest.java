package com.myZipPlan.server.docs;
import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.advice.loanAdvice.controller.LoanAdviceApiController;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceRequest;
import com.myZipPlan.server.advice.loanAdvice.dto.request.SimpleLoanAdviceRequest;
import com.myZipPlan.server.advice.loanAdvice.dto.request.SpecificLoanAdviceRequest;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.myZipPlan.server.advice.loanAdvice.dto.response.RecommendedProductDto;
import com.myZipPlan.server.advice.loanAdvice.service.LoanAdviceQueryService;
import com.myZipPlan.server.advice.loanAdvice.service.LoanAdviceService;
import com.myZipPlan.server.common.enums.Bank;
import com.myZipPlan.server.common.enums.loanAdvice.ChildStatus;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseHouseOwnershipType;
import com.myZipPlan.server.common.enums.loanAdvice.MaritalStatus;
import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.housingInfo.buildingInfo.common.RentHousingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoanAdviceApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/loanAdvice";
    private final LoanAdviceService loanAdviceService = mock(LoanAdviceService.class);
    private final LoanAdviceQueryService loanAdviceQueryService = mock(LoanAdviceQueryService.class);


    @Override
    protected Object initController() {
        return new LoanAdviceApiController(loanAdviceService, loanAdviceQueryService);
    }

    @DisplayName("최대한도최저금리 사전 조회")
    @Test
    void getSimpleLoanConditions() throws Exception {

        SimpleLoanAdviceRequest request = SimpleLoanAdviceRequest.builder()
            .rentalDeposit(BigDecimal.valueOf(200000000))
            .build();

        List<LoanAdviceSummaryResponse> response = Arrays.asList(
            LoanAdviceSummaryResponse.builder()
                .loanAdviceResultId(1L)
                .loanProductName("서울시신혼부부임차보증금대출")
                .loanProductCode("HF-001")
                .possibleLoanLimit(BigDecimal.valueOf(300000000L))
                .expectedLoanRate(BigDecimal.valueOf(2.5))
                .build(),
            LoanAdviceSummaryResponse.builder()
                .loanAdviceResultId(2L)
                .loanProductName("신혼부부전용전세자금대출")
                .loanProductCode("NHUF-001")
                .possibleLoanLimit(BigDecimal.valueOf(180000000L))
                .expectedLoanRate(BigDecimal.valueOf(2.1))
                .build()
        );

        when(loanAdviceService.getSimpleLoanConditions(any())).thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/simple")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(document("loan-Advice/get-simple-loan-conditions",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("rentalDeposit").type(JsonFieldType.NUMBER).description("임차보증금")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                    fieldWithPath("data[].loanAdviceResultId").type(JsonFieldType.NUMBER).description("대출 상담 결과 ID"),
                    fieldWithPath("data[].loanProductName").type(JsonFieldType.STRING).description("대출 상품명"),
                    fieldWithPath("data[].loanProductCode").type(JsonFieldType.STRING).description("대출 상품 코드"),
                    fieldWithPath("data[].possibleLoanLimit").type(JsonFieldType.NUMBER).description("가능한 대출 한도"),
                    fieldWithPath("data[].expectedLoanRate").type(JsonFieldType.NUMBER).description("예상 대출 금리")
                )
            ));
    }


    @DisplayName("전세대출 추천보고서 생성")
    @Test
    void generateLoanAdvice() throws Exception {
        LoanAdviceRequest request = createSampleLoanAdviceRequest();
        LoanAdviceResponse response = createSampleLoanAdviceResponse();

        when(loanAdviceService.createLoanAdvice(any())).thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("AccessToken", "액세스 토큰")
                .header("tempUserId", "일회성 유저 ID: \"temp\"로 시작하는 String")
            )
            .andExpect(status().isOk())
            .andDo(document("loan-advice/generate-loan-advice",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰"),
                    headerWithName("tempUserId")
                        .description("일회성 유저 ID")
                ),
                requestFields(
                    fieldWithPath("rentalDeposit").type(JsonFieldType.NUMBER).description("임차보증금"),
                    fieldWithPath("monthlyRent").type(JsonFieldType.NUMBER).description("월세"),
                    fieldWithPath("cashOnHand").type(JsonFieldType.NUMBER).description("보유현금"),
                    fieldWithPath("age").type(JsonFieldType.NUMBER).description("만나이"),
                    fieldWithPath("maritalStatus").type(JsonFieldType.STRING).description("혼인 상태 (SINGLE, ENGAGED, NEWLY_MARRIED, MARRIED)"),
                    fieldWithPath("annualIncome").type(JsonFieldType.NUMBER).description("연소득"),
                    fieldWithPath("spouseAnnualIncome").type(JsonFieldType.NUMBER).description("배우자연소득"),
                    fieldWithPath("childStatus").type(JsonFieldType.STRING).description("자녀 상태 (NO_CHILD, ONE_CHILD, TWO_CHILD, THREE_OR_MORE_CHILDREN)"),
                    fieldWithPath("hasNewborn").type(JsonFieldType.BOOLEAN).description("신생아여부"),
                    fieldWithPath("houseOwnershipType").type(JsonFieldType.STRING)
                        .description("주택 소유 형태 (NO_HOUSE: 무주택, SINGLE_HOUSE: 1주택, MULTI_HOUSE: 다주택"),
                    fieldWithPath("isSMEEmployee").type(JsonFieldType.BOOLEAN).description("중소기업재직여부"),
                    fieldWithPath("isNetAssetOver345M").type(JsonFieldType.BOOLEAN).description("순자산 3.45억 초과 여부"),
                    fieldWithPath("rentHousingType").type(JsonFieldType.STRING)
                        .description("주택 유형 (APARTMENT, DETACHED_HOUSE, MULTI_FAMILY_HOUSE, MULTI_HOUSEHOLD_HOUSE, OFFICETEL, OTHER)"),
                    fieldWithPath("exclusiveArea").type(JsonFieldType.NUMBER).description("전용면적"),
                    fieldWithPath("buildingName").type(JsonFieldType.STRING).description("건물명"),
                    fieldWithPath("districtCode").type(JsonFieldType.STRING).description("법정동 코드"),
                    fieldWithPath("dongName").type(JsonFieldType.STRING).description("읍면동 이름"),
                    fieldWithPath("jibun").type(JsonFieldType.STRING).description("지번")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.loanAdviceResultId").type(JsonFieldType.NUMBER).description("대출 상담 결과 ID"),
                    fieldWithPath("data.userInputInfoId").type(JsonFieldType.NUMBER).description("유저 인풋 정보 ID"),
                    fieldWithPath("data.hasEligibleProduct").type(JsonFieldType.BOOLEAN).description("대출 상품 추천 가능 여부"),
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
                    fieldWithPath("data.guaranteeInsuranceFee").type(JsonFieldType.NUMBER).description("보증 보험료"),
                    fieldWithPath("data.stampDuty").type(JsonFieldType.NUMBER).description("인지세"),
                    fieldWithPath("data.recommendationReason").type(JsonFieldType.STRING).description("추천 이유"),
                    fieldWithPath("data.recommendedProducts").type(JsonFieldType.ARRAY).description("추천 상품 목록"),
                    fieldWithPath("data.recommendedProducts[].loanProductName").type(JsonFieldType.STRING).description("대출 상품명"),
                    fieldWithPath("data.recommendedProducts[].loanProductCode").type(JsonFieldType.STRING).description("대출 상품 코드"),
                    fieldWithPath("data.recommendedProducts[].possibleLoanLimit").type(JsonFieldType.NUMBER).description("가능한 대출 한도"),
                    fieldWithPath("data.recommendedProducts[].expectedLoanRate").type(JsonFieldType.NUMBER).description("예상 대출 금리"),
                    fieldWithPath("data.recommendedProducts[].notEligibleReasons").type(JsonFieldType.ARRAY).optional().description("부적격 사유"),
                    fieldWithPath("data.availableBanks").type(JsonFieldType.ARRAY).description("이용 가능한 은행 목록"),
                    fieldWithPath("data.productFeatures").type(JsonFieldType.ARRAY).description("상품 특징"),
                    fieldWithPath("data.rentalLoanGuide").type(JsonFieldType.STRING).description("전세 대출 가이드")
                )
            ));
    }

    @DisplayName("특정 대출상품에 대한 보고서 생성")
    @Test
    void generateLoanAdviceOnSpecificLoan() throws Exception {
        LoanAdviceResponse response = createSampleLoanAdviceResponse();

        SpecificLoanAdviceRequest request = SpecificLoanAdviceRequest.builder()
            .userInputInfoId(1L)
            .productCode("HF0001")
            .build();

        when(loanAdviceService.generateLoanAdviceOnSpecificLoan(anyLong(), anyString())).thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/specific")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("AccessToken", "액세스 토큰")
            )
            .andExpect(status().isOk())
            .andDo(document("loan-advice/generate-loan-advice-on-specific-loan",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("userInputInfoId").type(JsonFieldType.NUMBER)
                        .description("기존 대출 상담 결과 ID"),
                    fieldWithPath("productCode").type(JsonFieldType.STRING)
                        .description("전세 상품 코드")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답님 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.loanAdviceResultId").type(JsonFieldType.NUMBER).description("대출 상담 결과 ID"),
                    fieldWithPath("data.userInputInfoId").type(JsonFieldType.NUMBER).description("유저 인풋 정보 ID"),
                    fieldWithPath("data.hasEligibleProduct").type(JsonFieldType.BOOLEAN).description("대출 상품 추천 가능 여부"),
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
                    fieldWithPath("data.guaranteeInsuranceFee").type(JsonFieldType.NUMBER).description("보증 보험료"),
                    fieldWithPath("data.stampDuty").type(JsonFieldType.NUMBER).description("인지세"),
                    fieldWithPath("data.recommendationReason").type(JsonFieldType.STRING).description("추천 이유"),
                    fieldWithPath("data.recommendedProducts").type(JsonFieldType.ARRAY).description("추천 상품 목록"),
                    fieldWithPath("data.recommendedProducts[].loanProductName").type(JsonFieldType.STRING).description("대출 상품명"),
                    fieldWithPath("data.recommendedProducts[].loanProductCode").type(JsonFieldType.STRING).description("대출 상품 코드"),
                    fieldWithPath("data.recommendedProducts[].possibleLoanLimit").type(JsonFieldType.NUMBER).description("가능한 대출 한도"),
                    fieldWithPath("data.recommendedProducts[].expectedLoanRate").type(JsonFieldType.NUMBER).description("예상 대출 금리"),
                    fieldWithPath("data.recommendedProducts[].notEligibleReasons").type(JsonFieldType.ARRAY).optional().description("부적격 사유 (해당되는 경우)"),
                    fieldWithPath("data.availableBanks").type(JsonFieldType.ARRAY).description("이용 가능한 은행 목록"),
                    fieldWithPath("data.productFeatures").type(JsonFieldType.ARRAY).description("상품 특징"),
                    fieldWithPath("data.rentalLoanGuide").type(JsonFieldType.STRING).description("전세 대출 가이드")
                )
            ));
    }


    private LoanAdviceRequest createSampleLoanAdviceRequest() {
        return LoanAdviceRequest.builder()
            .rentalDeposit(BigDecimal.valueOf(200000000))
            .monthlyRent(BigDecimal.valueOf(0))
            .cashOnHand(BigDecimal.valueOf(20000000))
            .age(30)
            .maritalStatus(MaritalStatus.MARRIED)
            .annualIncome(BigDecimal.valueOf(50000000))
            .spouseAnnualIncome(BigDecimal.valueOf(40000000))
            .childStatus(ChildStatus.ONE_CHILD)
            .hasNewborn(true)
            .houseOwnershipType(JeonseHouseOwnershipType.NO_HOUSE)
            .isSMEEmployee(false)
            .isNetAssetOver345M(false)
            .rentHousingType(RentHousingType.APARTMENT)
            .exclusiveArea(BigDecimal.valueOf(75.0))
            .buildingName("Sample Apartment")
            .districtCode("1168010100")
            .dongName("삼성동")
            .jibun("79-1")
            .build();
    }

    private LoanAdviceResponse createSampleLoanAdviceResponse() {
        return LoanAdviceResponse.builder()
            .loanAdviceResultId(1L)
            .userInputInfoId(1L)
            .hasEligibleProduct(true)
            .loanProductName("샘플 전세자금대출")
            .loanProductCode("SAMPLE001")
            .possibleLoanLimit(BigDecimal.valueOf(200000000))
            .expectedLoanRate(BigDecimal.valueOf(3.5))
            .totalRentalDeposit(BigDecimal.valueOf(300000000))
            .loanAmount(BigDecimal.valueOf(200000000))
            .ownFunds(BigDecimal.valueOf(100000000))
            .monthlyInterestCost(BigDecimal.valueOf(583333))
            .monthlyRent(BigDecimal.valueOf(0L))
            .totalLivingCost(BigDecimal.valueOf(583333L))
            .opportunityCostOwnFunds(BigDecimal.valueOf(100000000))
            .depositInterestRate(BigDecimal.valueOf(2.5))
            .guaranteeInsuranceFee(BigDecimal.valueOf(1000000))
            .stampDuty(BigDecimal.valueOf(150000))
            .recommendationReason("고객님의 소득과 신용도를 고려하여 가장 적합한 상품으로 선정되었습니다.")
            .recommendedProducts(Arrays.asList(
                RecommendedProductDto.builder()
                    .loanProductName("신혼부부전용전세자금대출")
                    .loanProductCode("HF-001")
                    .possibleLoanLimit(BigDecimal.valueOf(180000000))
                    .expectedLoanRate(BigDecimal.valueOf(3.7))
                    .notEligibleReasons(List.of())
                    .build(),
                RecommendedProductDto.builder()
                    .loanProductName("서울시신혼부부임차보증금대출")
                    .loanProductCode("HF-002")
                    .possibleLoanLimit(BigDecimal.valueOf(220000000))
                    .expectedLoanRate(BigDecimal.valueOf(3.8))
                    .notEligibleReasons(List.of("임차목적지가 서울시가 아닙니다."))
                    .build()
            ))
            .availableBanks(Arrays.asList(Bank.KB, Bank.SHINHAN, Bank.WOORI))
            .productFeatures(Arrays.asList("신혼부부추천", "낮은금리", "최대한도"))
            .rentalLoanGuide("전세자금대출 이용 시 주의사항:\n1. 대출 기간 동안 이자를 꾸준히 납부해야 합니다.\n2. 전세 계약 만료 시 대출금 상환 계획을 미리 세워야 합니다.")
            .build();
    }
}
