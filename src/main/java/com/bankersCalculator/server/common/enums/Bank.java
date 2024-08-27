package com.bankersCalculator.server.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Bank {
    HANA("하나은행"),
    KB("국민은행"),
    WOORI("우리은행"),
    SHINHAN("신한은행"),
    NH("신한은행"),
    KAKAO("카카오뱅크"),
    TOSS("토스뱅크"),
    IM("iM뱅크"),
    BNK("BNK부산은행");

    private final String description;

}
