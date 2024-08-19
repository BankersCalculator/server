package com.bankersCalculator.server.advise.userInputInfo.dto;

import com.bankersCalculator.server.advise.userInputInfo.domain.UserInputInfo;
import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInputInfoResponse {

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
            .hasNewborn(info.isHasNewborn())
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
