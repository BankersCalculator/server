package com.myZipPlan.server.oauth.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "tempUser", timeToLive = 604800)
public class TempUserToken {

    private Long id;

    @Indexed
    private String tempUserId;

}
