package com.bankersCalculator.server.advice.loanAdvice.dto;

import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;


@Getter
@Builder
public class LoanAdviceRequest {

    // 고객 정보
    @NotNull(message = "임차보증금은 필수 입력값입니다.")
    @Min(value = 1000000, message = "임차보증금은 1백만원 이상이어야 합니다.")
    private Long rentalDeposit; // 임차보증금

    @PositiveOrZero(message = "월세는 0 이상이어야 합니다.")
    private Long monthlyRent;   // 월세

    @PositiveOrZero(message = "보유현금은 0 이상이어야 합니다.")
    private Long cashOnHand;    // 보유현금

    @NotNull(message = "나이는 필수 입력값입니다.")
    @Range(min = 15, max = 100, message = "나이는 15세 이상 100세 이하여야 합니다.")
    private Integer age;    // 만나이

    @NotNull(message = "혼인상태는 필수 입력값입니다.")
    private MaritalStatus maritalStatus; // 혼인상태

    @NotNull(message = "연소득은 필수 입력값입니다.")
    @PositiveOrZero(message = "연소득은 0 이상이어야 합니다.")
    private Long annualIncome;  //연소득

    @PositiveOrZero(message = "배우자 연소득은 0 이상이어야 합니다.")
    private Long spouseAnnualIncome;    // 배우자연소득

    @NotNull(message = "자녀상태는 필수 입력값입니다.")
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


    public LoanAdviceServiceRequest toServiceRequest() {

        return LoanAdviceServiceRequest.builder()
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