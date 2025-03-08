package com.myZipPlan.server.user.repository;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@RedisHash(value = "guestUsage", timeToLive = 604800)
public class GuestUsage {

    private Long id;

    @Indexed
    private String guestId;

    private Integer count;

}
