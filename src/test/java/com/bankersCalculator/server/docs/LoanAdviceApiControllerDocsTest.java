package com.bankersCalculator.server.docs;

import com.bankersCalculator.server.RestDocsSupport;
import com.bankersCalculator.server.advice.loanAdvice.controller.LoanAdviceApiController;
import com.bankersCalculator.server.advice.loanAdvice.dto.LoanAdviceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.LoanAdviceResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.RecommendedProductDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.SpecificLoanAdviceRequest;
import com.bankersCalculator.server.advice.loanAdvice.service.LoanAdviceService;
import com.bankersCalculator.server.common.enums.Bank;
import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;

import static com.bankersCalculator.server.common.enums.loanAdvise.UserType.MEMBER;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoanAdviceApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/loanAdvice";
    private final LoanAdviceService loanAdviceService = mock(LoanAdviceService.class);

    @Override
    protected Object initController() {
        return new LoanAdviceApiController(loanAdviceService);
    }


    @DisplayName("대출 상담 결과 생성")
    @Test
    void generateLoanAdvice() throws Exception {
        LoanAdviceRequest request = createSampleLoanAdviceRequest();
        LoanAdviceResponse response = createSampleLoanAdviceResponse();

        when(loanAdviceService.generateLoanAdvice(any())).thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("accessToken", "액세스 토큰")
                .header("refreshToken", "리프레시 토큰")
                .header("tempUserId", "일회성 유저 ID")
            )
            .andExpect(status().isOk())
            .andDo(document("loan-Advice/generate-loan-Advice",
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
                    fieldWithPath("rentalDeposit").type(JsonFieldType.NUMBER).description("임차보증금"),
                    fieldWithPath("monthlyRent").type(JsonFieldType.NUMBER).description("월세"),
                    fieldWithPath("cashOnHand").type(JsonFieldType.NUMBER).description("보유현금"),
                    fieldWithPath("age").type(JsonFieldType.NUMBER).description("만나이"),
                    fieldWithPath("maritalStatus").type(JsonFieldType.STRING).description("혼인상태 (SINGLE, MARRIED, ENGAGED)"),
                    fieldWithPath("annualIncome").type(JsonFieldType.NUMBER).description("연소득"),
                    fieldWithPath("spouseAnnualIncome").type(JsonFieldType.NUMBER).description("배우자연소득"),
                    fieldWithPath("childStatus").type(JsonFieldType.STRING).description("자녀상태 (NO_CHILD, ONE_CHILD, TWO_CHILD, THREE_OR_MORE_CHILDREN)"),
                    fieldWithPath("hasNewborn").type(JsonFieldType.BOOLEAN).description("신생아여부"),
                    fieldWithPath("isSMEEmployee").type(JsonFieldType.BOOLEAN).description("중소기업재직여부"),
                    fieldWithPath("isNetAssetOver345M").type(JsonFieldType.BOOLEAN).description("순자산 3.45억 초과 여부"),
                    fieldWithPath("rentHousingType").type(JsonFieldType.STRING).description("주택 유형 (APARTMENT, DETACHED_HOUSE, MULTI_FAMILY_HOUSE, MULTI_HOUSEHOLD_HOUSE, OFFICETEL, OTHER)"),
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
    void generateLoanAdviceOnSpecificLoan() throws Exception {
        Long userId = 1L;
        Long loanAdviceResultId = 1L;
        LoanAdviceResponse response = createSampleLoanAdviceResponse();

        SpecificLoanAdviceRequest request = SpecificLoanAdviceRequest.builder()
            .loanAdviceResultId(200L)
            .productCode("HF0001")
            .build();

        when(loanAdviceService.generateLoanAdviceOnSpecificLoan(anyLong(), anyString())).thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/specific")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("accessToken", "액세스 토큰")
                .header("refreshToken", "리프레시 토큰")
            )
            .andExpect(status().isOk())
            .andDo(document("loan-Advice/generate-loan-Advice-on-specific-loan",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("accessToken")
                        .description("액세스 토큰"),
                    headerWithName("refreshToken")
                        .description("리프레쉬 토큰")
                ),
                requestFields(
                    fieldWithPath("loanAdviceResultId").type(JsonFieldType.NUMBER)
                        .description("기존 대출 상담 결과 ID"),
                    fieldWithPath("productCode").type(JsonFieldType.STRING)
                        .description("전세 상품 코드")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.loanAdviceResultId").type(JsonFieldType.NUMBER).description("대출 상담 결과 ID"),
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


    private LoanAdviceRequest createSampleLoanAdviceRequest() {
        return LoanAdviceRequest.builder()
            .rentalDeposit(200000000L)
            .monthlyRent(0L)
            .cashOnHand(20000000L)
            .age(30)
            .maritalStatus(MaritalStatus.MARRIED)
            .annualIncome(50000000L)
            .spouseAnnualIncome(40000000L)
            .childStatus(ChildStatus.ONE_CHILD)
            .hasNewborn(true)
            .isSMEEmployee(false)
            .isNetAssetOver345M(false)
            .rentHousingType(RentHousingType.APARTMENT)
            .exclusiveArea(75L)
            .buildingName("Sample Apartment")
            .districtCode("1168010100")
            .dongName("삼성동")
            .jibun("79-1")
            .build();
    }

    private LoanAdviceResponse createSampleLoanAdviceResponse() {
        return LoanAdviceResponse.builder()
            .loanAdviceResultId(1L)
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
