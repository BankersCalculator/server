package com.bankersCalculator.server.common.enums.calculator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoanType {
    MORTGAGE("주택담보대출"),
    INTERIM_PAYMENT_AND_MOVING("중도금 및 이주비"),
    OFFICETEL_MORTGAGE_LOAN("오피스텔담보대출"),
    JEONSE_LOAN("전세대출"),    // 전세는 우리나라만의 제도라 영문명칭도 JEONSE라고 함.
    JEONSE_DEPOSIT_COLLATERAL_LOAN("전세보증금담보대출"),
    PERSONAL_LOAN("신용대출"),
    NON_HOUSING_REAL_ESTATE_COLLATERAL_LOAN("비주택 부동산 담보 대출"),
    OTHER_COLLATERAL_LOAN("기타담보 대출"),
    DEPOSIT_AND_INSURANCE_COLLATERAL_LOAN("예적금 담보 및 보험계약 대출"),
    SECURITIES_COLLATERAL_LOAN("유가증권 담보대출"),
    LONG_TERM_CARD_LOAN("장기카드대출"),
    OTHER_LOAN("기타대출");

    private final String description;
}