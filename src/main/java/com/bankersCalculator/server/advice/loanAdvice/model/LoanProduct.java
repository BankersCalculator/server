package com.bankersCalculator.server.advice.loanAdvice.model;

import com.bankersCalculator.server.advice.loanAdvice.dto.api.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.service.FilterProductResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.service.LoanLimitAndRateResultDto;
import com.bankersCalculator.server.common.enums.JeonseLoanProductType;

public interface LoanProduct {

    /*
     * 구현 예정 전세상품 정리
     *
     * - 주택금융공사 -
     * 서울시신혼부부임차보증금대출
     * 서울시청년임차보증금대출
     * 주택신보전세자금대출
     * 청년전세론
     * (특례)무주택청년
     * (특례)다자녀가구
     * 고정금리 협약전세자금보증
     * 주택도시보증공사
     *
     * - 주택도시기금 -
     * 신생아특례버팀목전세자금대출
     * 청년전용버팀목전세자금대출
     * 중소기업취업청년전월세대출
     * 신혼부부전용전세자금대출
     * 버팀목전세자금
     *
     * - 서울보증보험 -
     * 우량주택전세론
     *
     * - 도시보증공사 -
     * 전세금안심대출
     */

    JeonseLoanProductType getProductType();

    String getProperty();

    FilterProductResultDto filtering(LoanAdviceServiceRequest request);

    LoanLimitAndRateResultDto calculateLoanLimitAndRate(LoanAdviceServiceRequest request);

}
