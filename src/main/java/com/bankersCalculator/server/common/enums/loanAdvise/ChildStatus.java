package com.bankersCalculator.server.common.enums.loanAdvise;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChildStatus {

    NO_CHILD("무자녀"),
    ONE_CHILD("1자녀"),
    TWO_CHILD_O("2자녀"),
    THREE_OR_MORE_CHILDREN("3자녀 이상");

    private final String description;


}
