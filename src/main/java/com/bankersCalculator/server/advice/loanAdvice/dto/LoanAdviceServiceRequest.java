package com.bankersCalculator.server.advice.loanAdvice.dto;

import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoanAdviceServiceRequest {


    // 고객 정보
    private Long rentalDeposit; // 임차보증금
    private Long monthlyRent;   // 월세
    private Long cashOnHand;    // 보유현금
    private Integer age;    // 만나이
    private MaritalStatus maritalStatus; // 혼인상태
    private Long annualIncome;  //연소득
    private Long spouseAnnualIncome;    // 배우자연소득
    private ChildStatus childStatus;    // 자녀상태
    private Boolean hasNewborn; // 신생아여부
    private Boolean isSMEEmployee; // 중소기업재직여부(SME: SmallMediumEnterprise);
    private Boolean isNetAssetOver345M; // 순자산 3.45억 초과 여부

    // 주택정보
    private RentHousingType rentHousingType;  // 주택타입
    private Double exclusiveArea; // 전용면적
    private String buildingName;   // 건물명
    private String districtCode; // 법정동 코드
    private String dongName;   // 읍명동이름
    private String jibun;   // 지번


}
