package com.myZipPlan.server.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleType {
    TEMP_USER("ROLE_TEMP_USER"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    NONE("NONE");

    private final String code;

}
