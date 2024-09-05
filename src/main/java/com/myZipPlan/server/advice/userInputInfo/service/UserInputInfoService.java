package com.myZipPlan.server.advice.userInputInfo.service;

import com.myZipPlan.server.advice.userInputInfo.dto.UserInputInfoResponse;
import com.myZipPlan.server.advice.userInputInfo.dto.UserInputSummaryResponse;
import com.myZipPlan.server.common.enums.loanAdvice.ChildStatus;
import com.myZipPlan.server.common.enums.loanAdvice.MaritalStatus;
import com.myZipPlan.server.housingInfo.buildingInfo.common.RentHousingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserInputInfoService {

    public UserInputInfoResponse getRecentlySubmittedUserInput() {

        return UserInputInfoResponse.builder()
            .rentalDeposit(BigDecimal.valueOf(300000000))  // 3억원 임차보증금
            .monthlyRent(BigDecimal.valueOf(500000))       // 50만원 월세
            .cashOnHand(BigDecimal.valueOf(50000000))      // 5천만원 보유 현금
            .age(35)                    // 35세
            .maritalStatus(MaritalStatus.MARRIED)
            .annualIncome(BigDecimal.valueOf(60000000))    // 6천만원 연소득
            .spouseAnnualIncome(BigDecimal.valueOf(40000000))  // 4천만원 배우자 연소득
            .childStatus(ChildStatus.ONE_CHILD)
            .hasNewborn(true)
            .isSMEEmployee(true)        // 중소기업 재직 여부
            .isNetAssetOver345M(false)  // 순자산 3.45억 초과 여부
            .rentHousingType(RentHousingType.APARTMENT)
            .exclusiveArea(BigDecimal.valueOf(85.0))         // 85제곱미터 전용면적
            .buildingName("행복아파트")
            .districtCode("1168010100") // 서울특별시 강남구 삼성동
            .dongName("삼성동")
            .jibun("79-1")
            .build();
    }

    public UserInputInfoResponse getSpecificUserInput(Long userInfoInputId) {

        return UserInputInfoResponse.builder()
            .rentalDeposit(BigDecimal.valueOf(300000000))  // 3억원 임차보증금
            .monthlyRent(BigDecimal.valueOf(500000))       // 50만원 월세
            .cashOnHand(BigDecimal.valueOf(50000000))      // 5천만원 보유 현금
            .age(35)                    // 35세
            .maritalStatus(MaritalStatus.MARRIED)
            .annualIncome(BigDecimal.valueOf(60000000))    // 6천만원 연소득
            .spouseAnnualIncome(BigDecimal.valueOf(40000000))  // 4천만원 배우자 연소득
            .childStatus(ChildStatus.ONE_CHILD)
            .hasNewborn(true)
            .isSMEEmployee(true)        // 중소기업 재직 여부
            .isNetAssetOver345M(false)  // 순자산 3.45억 초과 여부
            .rentHousingType(RentHousingType.APARTMENT)
            .exclusiveArea(BigDecimal.valueOf(85.0))         // 85제곱미터 전용면적
            .buildingName("행복아파트")
            .districtCode("1168010100") // 서울특별시 강남구 삼성동
            .dongName("삼성동")
            .jibun("79-1")
            .build();
    }

    public List<UserInputSummaryResponse> getRecentUserInputs() {
        return null;
    }
}
