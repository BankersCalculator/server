package com.myZipPlan.server.oauth.token;


import com.myZipPlan.server.common.enums.ABTestType;
import com.myZipPlan.server.common.enums.RoleType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDto {

    private final String accessToken;
    private final String refreshToken;
    private final RoleType roleType;
    private ABTestType abTestType;

    public void setAbTestType(ABTestType abTestType) {
        this.abTestType = abTestType;
    }
}
