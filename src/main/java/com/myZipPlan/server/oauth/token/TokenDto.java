package com.myZipPlan.server.oauth.token;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDto {

    private final String accessToken;
    private final String refreshToken;
}
