package com.myZipPlan.server.oauth.userInfo;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class UserProfile {

    private String nickname;
    private String email;
    private String thumbnailImage;

}
