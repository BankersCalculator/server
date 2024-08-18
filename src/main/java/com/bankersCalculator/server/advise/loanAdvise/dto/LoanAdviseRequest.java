package com.bankersCalculator.server.advise.loanAdvise.dto;

import com.bankersCalculator.server.common.enums.loanAdvise.AreaSize;
import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.common.enums.ltv.HousingType;
import com.bankersCalculator.server.common.enums.ltv.RegionType;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class LoanAdviseRequest {

    // 고객 정보
    private long rentalDeposit; // 임차보증금
    private long monthlyRent;   // 월세
    private long cashOnHand;    // 보유현금
    private int age;    // 만나이
    private MaritalStatus maritalStatus; // 혼인상태
    private long annualIncome;  //연소득
    private long spouseAnnualIncome;    // 배우자연소득
    private ChildStatus childStatus;    // 자녀상태
    private boolean hasNewborn; // 신생아여부
    private Boolean isSMEEmployee; // 중소기업재직여부(SME: SmallMediumEnterprise);
    private Boolean isNetAssetOver345M; // 순자산 3.45억 초과 여부


    // 주택정보
    private RentHousingType rentHousingType;  // 주택타입
    private long exclusiveArea; // 전용면적
    private String buildingName;   // 건물명
    private String districtCode; // 법정동 코드
    private String dongName;   // 읍명동이름
    private String jibun;   // 지번


    public LoanAdviseServiceRequest toServiceRequest() {
        return LoanAdviseServiceRequest.builder()
            .rentalDeposit(rentalDeposit)
            .monthlyRent(monthlyRent)
            .cashOnHand(cashOnHand)
            .age(age)
            .maritalStatus(maritalStatus)
            .annualIncome(annualIncome)
            .spouseAnnualIncome(spouseAnnualIncome)
            .childStatus(childStatus)
            .hasNewborn(hasNewborn)
            .isSMEEmployee(isSMEEmployee)
            .isNetAssetOver345M(isNetAssetOver345M)
            .rentHousingType(rentHousingType)
            .exclusiveArea(exclusiveArea)
            .buildingName(buildingName)
            .districtCode(districtCode)
            .dongName(dongName)
            .jibun(jibun)
            .build();
    }
}