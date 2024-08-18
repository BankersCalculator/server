package com.bankersCalculator.server.advise.userInputInfo.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserInputSummary {

    private final String userInputInfoId;
    private final LocalDateTime inquiryDateTime; // 조회일시
    private String dongName;   // 읍명동이름
    private String buildingName;   // 건물명
    private long rentalDeposit; // 임차보증금
    private long monthlyRent;   // 월세

}
