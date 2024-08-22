package com.bankersCalculator.server.docs.calculator;

import com.bankersCalculator.server.RestDocsSupport;
import com.bankersCalculator.server.calculator.dtiCalc.controller.DtiCalcApiController;
import com.bankersCalculator.server.calculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcRequest;
import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcResponse;
import com.bankersCalculator.server.calculator.dtiCalc.service.DtiCalcService;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DtiCalcApiControllerDocsTest extends RestDocsSupport {

    private static final String BASE_URL = "/api/v1/dtiCalc";
    private final DtiCalcService dtiCalcService = mock(DtiCalcService.class);

    @Override
    protected Object initController() {
        return new DtiCalcApiController(dtiCalcService);
    }

    @DisplayName("DTI 계산기 API")
    @Test
    void calculateDti() throws Exception {
        // 요청 객체 생성
        DtiCalcRequest request = new DtiCalcRequest();
        DtiCalcRequest.LoanStatus loanStatus = new DtiCalcRequest.LoanStatus();
        loanStatus.setRepaymentType(RepaymentType.AMORTIZING);
        loanStatus.setLoanType(LoanType.MORTGAGE);
        loanStatus.setPrincipal(300000000.0);
        loanStatus.setTerm(360);
        loanStatus.setInterestRatePercentage(3.5);
        request.getLoanStatuses().add(loanStatus);
        request.setAnnualIncome(50000000);

        // 응답 객체 생성
        DtiCalcResponse response = DtiCalcResponse.builder()
                .annualIncome(50000000)
                .totalLoanCount(1)
                .dtiCalcResults(Arrays.asList(
                        DtiCalcResult.builder()
                                .serial(1)
                                .principal(300000000)
                                .balance(295000000)
                                .term(360)
                                .annualPrincipalRepayment(5000000)
                                .annualInterestRepayment(10500000)
                                .build()
                ))
                .finalDtiRatio(31.0)
                .build();


        when(dtiCalcService.dtiCalculate(any()))
                .thenReturn(response);

        // MockMvc 수행 및 문서화
        mockMvc.perform(post(BASE_URL)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("calculator/dti-calc",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("loanStatuses").type(JsonFieldType.ARRAY)
                                        .description("대출 상태 목록"),
                                fieldWithPath("loanStatuses[].repaymentType").type(JsonFieldType.STRING)
                                        .description("상환 유형 (BULLET: 일시상환, AMORTIZING: 원리금균등분할상환, EQUAL_PRINCIPAL: 원금균등분할상환)"),
                                fieldWithPath("loanStatuses[].loanType").type(JsonFieldType.STRING)
                                        .description("대출 유형 (MORTGAGE: 주택담보대출, INTERIM_PAYMENT_AND_MOVING: 중도금 및 이주비, " +
                                                "OFFICETEL_MORTGAGE_LOAN: 오피스텔담보대출, JEONSE_LOAN: 전세대출, " +
                                                "JEONSE_DEPOSIT_COLLATERAL_LOAN: 전세보증금담보대출, PERSONAL_LOAN: 신용대출, " +
                                                "NON_HOUSING_REAL_ESTATE_COLLATERAL_LOAN: 비주택 부동산 담보 대출, " +
                                                "OTHER_COLLATERAL_LOAN: 기타담보 대출, " +
                                                "DEPOSIT_AND_INSURANCE_COLLATERAL_LOAN: 예적금 담보 및 보험계약 대출, " +
                                                "SECURITIES_COLLATERAL_LOAN: 유가증권 담보대출, LONG_TERM_CARD_LOAN: 장기카드대출, " +
                                                "OTHER_LOAN: 기타대출)"),
                                fieldWithPath("loanStatuses[].principal").type(JsonFieldType.NUMBER)
                                        .description("원금"),
                                fieldWithPath("loanStatuses[].term").type(JsonFieldType.NUMBER)
                                        .description("기간(개월수)"),
                                fieldWithPath("loanStatuses[].interestRatePercentage").type(JsonFieldType.NUMBER)
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
                                fieldWithPath("data.dtiCalcResults").type(JsonFieldType.ARRAY)
                                        .description("DTI 계산 결과 목록"),
                                fieldWithPath("data.dtiCalcResults[].serial").type(JsonFieldType.NUMBER)
                                        .description("일련번호"),
                                fieldWithPath("data.dtiCalcResults[].principal").type(JsonFieldType.NUMBER)
                                        .description("원금"),
                                fieldWithPath("data.dtiCalcResults[].balance").type(JsonFieldType.NUMBER)
                                        .description("잔액"),
                                fieldWithPath("data.dtiCalcResults[].term").type(JsonFieldType.NUMBER)
                                        .description("기간"),
                                fieldWithPath("data.dtiCalcResults[].annualPrincipalRepayment").type(JsonFieldType.NUMBER)
                                        .description("연간 원금 상환액"),
                                fieldWithPath("data.dtiCalcResults[].annualInterestRepayment").type(JsonFieldType.NUMBER)
                                        .description("연간 이자 상환액"),
                                fieldWithPath("data.finalDtiRatio").type(JsonFieldType.NUMBER)
                                        .description("최종 DTI 비율")
                        )
                ));
    }
}
