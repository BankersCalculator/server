package com.bankersCalculator.server.docs;

import com.bankersCalculator.server.RestDocsSupport;
import com.bankersCalculator.server.calculator.dsrCalc.cotroller.DsrCalcApiController;
import com.bankersCalculator.server.calculator.dsrCalc.dto.DsrCalcRequest;
import com.bankersCalculator.server.calculator.dsrCalc.dto.DsrCalcResponse;
import com.bankersCalculator.server.calculator.dsrCalc.service.DsrCalcService;
import com.bankersCalculator.server.calculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.server.common.enums.LoanType;
import com.bankersCalculator.server.common.enums.RepaymentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DsrCalcApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/dsrCalc";
    private final DsrCalcService dsrCalcService = mock(DsrCalcService.class);

    @Override
    protected Object initController() {
        return new DsrCalcApiController(dsrCalcService);
    }

    @DisplayName("DSR 계산기 API")
    @Test
    void calculateDsr() throws Exception {
        DsrCalcRequest request = new DsrCalcRequest();
        DsrCalcRequest.LoanStatus loanStatus = new DsrCalcRequest.LoanStatus();
        loanStatus.setRepaymentType(RepaymentType.AMORTIZING);
        loanStatus.setLoanType(LoanType.MORTGAGE);
        loanStatus.setPrincipal(300000000);
        loanStatus.setMaturityPaymentAmount(0);
        loanStatus.setTerm(360);
        loanStatus.setGracePeriod(0);
        loanStatus.setInterestRatePercentage(3.5);
        request.getLoanStatusList().add(loanStatus);
        request.setAnnualIncome(50000000);

        DsrCalcResponse response = DsrCalcResponse.builder()
            .annualIncome(50000000)
            .totalLoanCount(1)
            .dsrCalcResultList(Arrays.asList(
                new DsrCalcResult(1, 300000000, 295000000, 360, 5000000, 10500000)
            ))
            .finalDsrRatio(31.0)
            .build();

        when(dsrCalcService.dsrCalculate(any()))
            .thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("AccessToken", "액세스 토큰")
                .header("RefreshToken", "리프레시 토큰")
            )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("calculator/dsr-calc",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("AccessToken")
                        .description("액세스 토큰"),
                    headerWithName("RefreshToken")
                        .description("리프레쉬 토큰")
                ),
                requestFields(
                    fieldWithPath("loanStatusList").type(JsonFieldType.ARRAY)
                        .description("대출 상태 목록"),
                    fieldWithPath("loanStatusList[].repaymentType").type(JsonFieldType.STRING)
                        .description("상환 유형 (BULLET: 일시상환, AMORTIZING: 원리금균등분할상환, EQUAL_PRINCIPAL: 원금균등분할상환)"),
                    fieldWithPath("loanStatusList[].loanType").type(JsonFieldType.STRING)
                        .description("대출 유형 (MORTGAGE: 주택담보대출, INTERIM_PAYMENT_AND_MOVING: 중도금 및 이주비, " +
                            "OFFICETEL_MORTGAGE_LOAN: 오피스텔담보대출, JEONSE_LOAN: 전세대출, " +
                            "JEONSE_DEPOSIT_COLLATERAL_LOAN: 전세보증금담보대출, PERSONAL_LOAN: 신용대출, " +
                            "NON_HOUSING_REAL_ESTATE_COLLATERAL_LOAN: 비주택 부동산 담보 대출, " +
                            "OTHER_COLLATERAL_LOAN: 기타담보 대출, " +
                            "DEPOSIT_AND_INSURANCE_COLLATERAL_LOAN: 예적금 담보 및 보험계약 대출, " +
                            "SECURITIES_COLLATERAL_LOAN: 유가증권 담보대출, LONG_TERM_CARD_LOAN: 장기카드대출, " +
                            "OTHER_LOAN: 기타대출)"),
                    fieldWithPath("loanStatusList[].principal").type(JsonFieldType.NUMBER)
                        .description("원금"),
                    fieldWithPath("loanStatusList[].maturityPaymentAmount").type(JsonFieldType.NUMBER)
                        .description("만기상환금액"),
                    fieldWithPath("loanStatusList[].term").type(JsonFieldType.NUMBER)
                        .description("기간(개월수)"),
                    fieldWithPath("loanStatusList[].gracePeriod").type(JsonFieldType.NUMBER)
                        .description("거치기간"),
                    fieldWithPath("loanStatusList[].interestRatePercentage").type(JsonFieldType.NUMBER)
                        .description("연이자율(%)"),
                    fieldWithPath("annualIncome").type(JsonFieldType.NUMBER)
                        .description("연간 소득")
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
                    fieldWithPath("data.annualIncome").type(JsonFieldType.NUMBER)
                        .description("연간 소득"),
                    fieldWithPath("data.totalLoanCount").type(JsonFieldType.NUMBER)
                        .description("총 대출 건수"),
                    fieldWithPath("data.dsrCalcResultList").type(JsonFieldType.ARRAY)
                        .description("DSR 계산 결과 목록"),
                    fieldWithPath("data.dsrCalcResultList[].serial").type(JsonFieldType.NUMBER)
                        .description("일련번호"),
                    fieldWithPath("data.dsrCalcResultList[].principal").type(JsonFieldType.NUMBER)
                        .description("원금"),
                    fieldWithPath("data.dsrCalcResultList[].balance").type(JsonFieldType.NUMBER)
                        .description("잔액"),
                    fieldWithPath("data.dsrCalcResultList[].term").type(JsonFieldType.NUMBER)
                        .description("기간"),
                    fieldWithPath("data.dsrCalcResultList[].annualPrincipalRepayment").type(JsonFieldType.NUMBER)
                        .description("연간 원금 상환액"),
                    fieldWithPath("data.dsrCalcResultList[].annualInterestRepayment").type(JsonFieldType.NUMBER)
                        .description("연간 이자 상환액"),
                    fieldWithPath("data.finalDsrRatio").type(JsonFieldType.NUMBER)
                        .description("최종 DSR 비율")
                )
            ));
    }
}