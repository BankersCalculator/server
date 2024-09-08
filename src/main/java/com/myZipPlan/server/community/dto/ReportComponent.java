package com.myZipPlan.server.community.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
//임시생성
public class ReportComponent {
    private final String title;          // 보고서 또는 광고 제목
    private final String description;    // 설명
    private final double interestRate;   // 이자율
    private final String amount;         // 금액

    @Builder
    public ReportComponent(String title, String description, double interestRate, String amount) {
        this.title = title;
        this.description = description;
        this.interestRate = interestRate;
        this.amount = amount;
    }
}
