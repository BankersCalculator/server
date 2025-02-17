package com.myZipPlan.server.advice.loanAdvice.dto.request;

import com.myZipPlan.server.common.enums.loanAdvice.ChildStatus;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseHouseOwnershipType;
import com.myZipPlan.server.common.enums.loanAdvice.MaritalStatus;
import com.myZipPlan.server.housingInfo.buildingInfo.common.RentHousingType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;


@Getter
@Builder
@ToString
public class PreLoanTermsRequest {

    private BigDecimal rentalDeposit; // 임차보증금
    private BigDecimal monthlyRent;   // 월세
    private BigDecimal cashOnHand;    // 보유현금
    private Integer age;    // 만나이
    private MaritalStatus maritalStatus; // 혼인상태
    private BigDecimal annualIncome;  //연소득
    private BigDecimal spouseAnnualIncome;    // 배우자연소득
    private ChildStatus childStatus;    // 자녀상태
    private Boolean hasNewborn; // 신생아여부
    private JeonseHouseOwnershipType houseOwnershipType; // 주택소유형태
    private Boolean isSMEEmployee; // 중소기업재직여부(SME: SmallMediumEnterprise);
    private Boolean isNetAssetOver345M; // 순자산 3.45억 초과 여부

    public LoanAdviceServiceRequest toServiceRequestWithDefaultValue() {
        return LoanAdviceServiceRequest.builder()
            .rentalDeposit(rentalDeposit != null ? rentalDeposit : BigDecimal.valueOf(300000000))
            .monthlyRent(monthlyRent != null ? monthlyRent : BigDecimal.ZERO)
            .cashOnHand(cashOnHand != null ? cashOnHand : BigDecimal.valueOf(100000000)) //TODO 수정 필요
            .age(age != null ? age : 30)
            .maritalStatus(maritalStatus != null ? maritalStatus : MaritalStatus.SINGLE)
            .annualIncome(annualIncome != null ? annualIncome : BigDecimal.valueOf(100000000))
            .spouseAnnualIncome(spouseAnnualIncome != null ? spouseAnnualIncome : BigDecimal.ZERO)
            .childStatus(childStatus != null ? childStatus : ChildStatus.ONE_CHILD)
            .hasNewborn(hasNewborn != null ? hasNewborn : true)
            .houseOwnershipType(houseOwnershipType != null ? houseOwnershipType : JeonseHouseOwnershipType.NO_HOUSE)
            .isSMEEmployee(isSMEEmployee != null ? isSMEEmployee : true)
            .isNetAssetOver345M(isNetAssetOver345M != null ? isNetAssetOver345M : false)
            .rentHousingType(RentHousingType.APARTMENT)
            .exclusiveArea( BigDecimal.valueOf(50))
            .buildingName( "행복아파트")
            .districtCode("1111011700")
            .dongName("역삼동")
            .jibun("649-5")
            .build();
    }
}