package com.bankersCalculator.server.advice.userInputInfo.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserInputSummaryResponse {

    private Long userInputInfoId;
    private LocalDateTime inquiryDateTime; // 조회일시
    private String dongName;   // 읍명동이름
    private String buildingName;   // 건물명
    private Long rentalDeposit; // 임차보증금
    private Long monthlyRent;   // 월세

}
