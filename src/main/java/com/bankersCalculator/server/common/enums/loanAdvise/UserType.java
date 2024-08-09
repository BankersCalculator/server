package com.bankersCalculator.server.common.enums.loanAdvise;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {

    MEMBER("회원"),
    NON_MEMBER("비회원");

    private String description;


}
