package com.bankersCalculator.server.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleType {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    NONE("NONE");

    private final String code;

}
