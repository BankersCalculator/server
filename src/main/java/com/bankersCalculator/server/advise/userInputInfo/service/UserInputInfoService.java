package com.bankersCalculator.server.advise.userInputInfo.service;

import com.bankersCalculator.server.advise.userInputInfo.dto.UserInputInfoResponse;
import com.bankersCalculator.server.advise.userInputInfo.dto.UserInputInfoServiceRequest;
import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserInputInfoService {

    public UserInputInfoResponse getRecentlySubmittedUserInput(UserInputInfoServiceRequest serviceRequest) {
        UserInputInfoResponse response = UserInputInfoResponse.builder()
            .rentalDeposit(300000000L)  // 3억원 임차보증금
            .monthlyRent(500000L)       // 50만원 월세
            .cashOnHand(50000000L)      // 5천만원 보유 현금
            .age(35)                    // 35세
            .maritalStatus(MaritalStatus.MARRIED)
            .annualIncome(60000000L)    // 6천만원 연소득
            .spouseAnnualIncome(40000000L)  // 4천만원 배우자 연소득
            .childStatus(ChildStatus.ONE_CHILD)
            .hasNewborn(true)
            .isSMEEmployee(true)        // 중소기업 재직 여부
            .isNetAssetOver345M(false)  // 순자산 3.45억 초과 여부
            .rentHousingType(RentHousingType.APARTMENT)
            .exclusiveArea(85L)         // 85제곱미터 전용면적
            .buildingName("행복아파트")
            .districtCode("1168010100") // 서울특별시 강남구 삼성동
            .dongName("삼성동")
            .jibun("79-1")
            .build();

        return response;
    }
}
