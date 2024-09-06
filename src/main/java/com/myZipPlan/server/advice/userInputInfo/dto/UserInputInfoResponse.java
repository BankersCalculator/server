package com.myZipPlan.server.advice.userInputInfo.dto;

import com.myZipPlan.server.advice.userInputInfo.entity.UserInputInfo;
import com.myZipPlan.server.common.enums.loanAdvice.ChildStatus;
import com.myZipPlan.server.common.enums.loanAdvice.MaritalStatus;
import com.myZipPlan.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class UserInputInfoResponse {

    // 고객 정보
    private BigDecimal rentalDeposit; // 임차보증금
    private BigDecimal monthlyRent;   // 월세
    private BigDecimal cashOnHand;    // 보유현금
    private Integer age;    // 만나이
    private MaritalStatus maritalStatus; // 혼인상태
    private BigDecimal annualIncome;  //연소득
    private BigDecimal spouseAnnualIncome;    // 배우자연소득
    private ChildStatus childStatus;    // 자녀상태
    private Boolean hasNewborn; // 신생아여부
    private Boolean isSMEEmployee; // 중소기업재직여부(SME: SmallMediumEnterprise);
    private Boolean isNetAssetOver345M; // 순자산 3.45억 초과 여부


    // 주택정보
    private RentHousingType rentHousingType;  // 주택타입
    private BigDecimal exclusiveArea; // 전용면적
    private String buildingName;   // 건물명
    private String districtCode; // 법정동 코드
    private String dongName;   // 읍명동이름
    private String jibun;   // 지번

    public static UserInputInfoResponse of(UserInputInfo info) {
        return UserInputInfoResponse.builder()
            .rentalDeposit(info.getRentalDeposit())
            .monthlyRent(info.getMonthlyRent())
            .cashOnHand(info.getCashOnHand())
            .age(info.getAge())
            .maritalStatus(info.getMaritalStatus())
            .annualIncome(info.getAnnualIncome())
            .spouseAnnualIncome(info.getSpouseAnnualIncome())
            .childStatus(info.getChildStatus())
            .hasNewborn(info.getHasNewborn())
            .isSMEEmployee(info.getIsSMEEmployee())
            .isNetAssetOver345M(info.getIsNetAssetOver345M())
            .rentHousingType(info.getRentHousingType())
            .exclusiveArea(info.getExclusiveArea())
            .buildingName(info.getBuildingName())
            .districtCode(info.getDistrictCode())
            .dongName(info.getDongName())
            .jibun(info.getJibun())
            .build();
    }

}
