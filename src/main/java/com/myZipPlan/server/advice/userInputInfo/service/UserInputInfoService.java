package com.myZipPlan.server.advice.userInputInfo.service;

import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.userInputInfo.dto.UserInputInfoResponse;
import com.myZipPlan.server.advice.userInputInfo.dto.UserInputSummaryResponse;
import com.myZipPlan.server.advice.userInputInfo.entity.UserInputInfo;
import com.myZipPlan.server.advice.userInputInfo.repository.UserInputInfoRepository;
import com.myZipPlan.server.common.exception.customException.AuthException;
import com.myZipPlan.server.oauth.userInfo.SecurityUtils;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserInputInfoService {

    private final UserInputInfoRepository userInputInfoRepository;
    private final UserRepository userRepository;


    public UserInputInfo findById(Long id) {
        return userInputInfoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("입력된 유저투입정보가 유효하지 않습니다."));
    }

    public UserInputInfo recordUserInput(LoanAdviceServiceRequest request, User user) {
        UserInputInfo userInputInfo = UserInputInfo.create(user, request);
        return save(userInputInfo);
    }

    public UserInputInfo save(UserInputInfo userInputInfo) {
        return userInputInfoRepository.save(userInputInfo);
    }

    public UserInputInfoResponse getRecentlySubmittedUserInput() {

        User user = fetchCurrentUser();

        return userInputInfoRepository.findTop1ByUserIdOrderByCreatedDateTimeDesc(user.getId())
            .map(this::mapToUserInputInfoResponse)
            .orElse(null);
    }

    public UserInputInfoResponse getSpecificUserInput(Long userInfoInputId) {

       return  userInputInfoRepository.findById(userInfoInputId)
            .map(this::mapToUserInputInfoResponse)
            .orElse(null);
    }

    public List<UserInputSummaryResponse> getRecentUserInputs() {
        User user = fetchCurrentUser();

        List<UserInputInfo> userInputInfoList = userInputInfoRepository.findTop5ByUserIdOrderByCreatedDateTimeDesc(user.getId());

        if (userInputInfoList.isEmpty()) {
            return null;
        }

        return userInputInfoList.stream()
            .map(userInputInfo -> UserInputSummaryResponse.builder()
                .userInputInfoId(userInputInfo.getId())
                .inquiryDateTime(userInputInfo.getCreatedDateTime())
                .dongName(userInputInfo.getDongName())
                .buildingName(userInputInfo.getBuildingName())
                .rentalDeposit(userInputInfo.getRentalDeposit())
                .monthlyRent(userInputInfo.getMonthlyRent())
                .build())
            .collect(Collectors.toList());
    }


    private User fetchCurrentUser() {
        String providerId = SecurityUtils.getProviderId();

        User user = userRepository.findByProviderId(providerId)
            .orElseThrow(() -> new AuthException("사용자 정보가 없습니다."));

        return user;
    }


    private UserInputInfoResponse mapToUserInputInfoResponse(UserInputInfo userInputInfo) {
        return UserInputInfoResponse.builder()
            .rentalDeposit(userInputInfo.getRentalDeposit())
            .monthlyRent(userInputInfo.getMonthlyRent())
            .cashOnHand(userInputInfo.getCashOnHand())
            .age(userInputInfo.getAge())
            .maritalStatus(userInputInfo.getMaritalStatus())
            .annualIncome(userInputInfo.getAnnualIncome())
            .spouseAnnualIncome(userInputInfo.getSpouseAnnualIncome())
            .childStatus(userInputInfo.getChildStatus())
            .hasNewborn(userInputInfo.getHasNewborn())
            .isSMEEmployee(userInputInfo.getIsSMEEmployee())
            .isNetAssetOver345M(userInputInfo.getIsNetAssetOver345M())
            .rentHousingType(userInputInfo.getRentHousingType())
            .exclusiveArea(userInputInfo.getExclusiveArea())
            .buildingName(userInputInfo.getBuildingName())
            .districtCode(userInputInfo.getDistrictCode())
            .dongName(userInputInfo.getDongName())
            .jibun(userInputInfo.getJibun())
            .build();
    }
}
