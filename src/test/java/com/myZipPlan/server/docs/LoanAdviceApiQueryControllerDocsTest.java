package com.myZipPlan.server.docs;

import com.myZipPlan.server.RestDocsSupport;
import com.myZipPlan.server.advice.loanAdvice.controller.LoanAdviceApiController;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.myZipPlan.server.advice.loanAdvice.dto.response.RecommendedProductDto;
import com.myZipPlan.server.advice.loanAdvice.service.LoanAdviceQueryService;
import com.myZipPlan.server.advice.loanAdvice.service.LoanAdviceService;
import com.myZipPlan.server.common.enums.Bank;
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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoanAdviceApiQueryControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/loanAdvice";
    private final LoanAdviceService loanAdviceService = mock(LoanAdviceService.class);
    private final LoanAdviceQueryService loanAdviceQueryService = mock(LoanAdviceQueryService.class);

    @Override
    protected Object initController() {
        return new LoanAdviceApiController(loanAdviceService, loanAdviceQueryService);
    }


    @DisplayName("최근 대출추천 보고서 목록 조회")
    @Test
    void getRecentLoanAdvices() throws Exception {

        List<LoanAdviceSummaryResponse> response = Arrays.asList(
            LoanAdviceSummaryResponse.builder()
                .loanAdviceResultId(1L)
                .loanProductName("서울시신혼부부임차보증금대출")
                .loanProductCode("HF-001")
                .possibleLoanLimit(BigDecimal.valueOf(200000000L))
                .expectedLoanRate(BigDecimal.valueOf(3.5))
                .build(),
            LoanAdviceSummaryResponse.builder()
                .loanAdviceResultId(2L)
                .loanProductName("신혼부부전용전세자금대출")
                .loanProductCode("NHUF-001")
                .possibleLoanLimit(BigDecimal.valueOf(170000000L))
                .expectedLoanRate(BigDecimal.valueOf(2.4))
                .build()
        );

        when(loanAdviceQueryService.getRecentLoanAdvices()).thenReturn(response);

        mockMvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("AccessToken", "액세스 토큰")
            )
            .andExpect(status().isOk())
            .andDo(document("loan-Advice/get-recent-loan-advices",
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
                    fieldWithPath("data[].loanAdviceResultId").type(JsonFieldType.NUMBER).description("대출 상담 결과 ID"),
                    fieldWithPath("data[].loanProductName").type(JsonFieldType.STRING).description("대출 상품명"),
                    fieldWithPath("data[].loanProductCode").type(JsonFieldType.STRING).description("대출 상품 코드"),
                    fieldWithPath("data[].possibleLoanLimit").type(JsonFieldType.NUMBER).description("가능한 대출 한도"),
                    fieldWithPath("data[].expectedLoanRate").type(JsonFieldType.NUMBER).description("예상 대출 금리")
                )
            ));

    }

    @DisplayName("특정 대출추천 보고서 조회")
    @Test
    void getSpecificLoanAdvice() throws Exception {
        LoanAdviceResponse response = createSampleLoanAdviceResponse();

        when(loanAdviceQueryService.getSpecificLoanAdvice(any())).thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/{loanAdviceResultId}", 200L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("AccessToken", "액세스 토큰")
            )
            .andExpect(status().isOk())
            .andDo(document("loan-advice/get-specific-loan-advice",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰")
                ),
                pathParameters(
                    parameterWithName("loanAdviceResultId").description("대출 상담 결과 ID")
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
