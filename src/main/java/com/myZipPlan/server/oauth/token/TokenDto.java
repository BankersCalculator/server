package com.myZipPlan.server.oauth.token;


import com.myZipPlan.server.common.enums.RoleType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDto {

    private final String accessToken;
    private final String refreshToken;
    private final RoleType roleType;
}
