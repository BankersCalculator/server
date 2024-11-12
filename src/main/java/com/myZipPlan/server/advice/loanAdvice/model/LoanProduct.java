package com.myZipPlan.server.advice.loanAdvice.model;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.common.enums.Bank;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseLoanProductType;

import java.math.BigDecimal;
import java.util.List;

public interface LoanProduct {

    /*
     * 구현 예정 전세상품 정리
     * (enum: JeonseLoanProductType)
     *
     * - 주택금융공사 -
     * 서울시신혼부부임차보증금대출  - 구현
     * 서울시청년임차보증금대출     - 구현
     * 주택신보전세자금대출      - 구현
     * 청년전세론    - 구현
     * 고정금리 협약전세자금보증   - 구현
     *
     * - 주택도시기금 -
     * 신생아특례버팀목전세자금대출  - 구현
     * 청년전용버팀목전세자금대출  - 구현
     * 중소기업취업청년전월세대출  - 구현
     * 신혼부부전용전세자금대출
     * 버팀목전세자금
     *
     * - 서울보증보험 -
     * 우량주택전세론  - 구현
     *
     * - 도시보증공사 -
     * 전세금안심대출  - 구현
     */

    JeonseLoanProductType getProductType();

    FilterProductResultDto filtering(LoanAdviceServiceRequest request);

    LoanLimitAndRateResultDto calculateLoanLimitAndRate(LoanAdviceServiceRequest request);

    LoanLimitAndRateResultDto calculateMaxLoanLimitAndMinRate(BigDecimal rentalAmount);

    BigDecimal getGuaranteeInsuranceFee(BigDecimal loanAmount);

    List<Bank> getAvailableBanks();

    List<String> getProductFeatures();

}
