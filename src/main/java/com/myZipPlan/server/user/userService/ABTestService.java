package com.myZipPlan.server.user.userService;

import com.myZipPlan.server.common.enums.ABTestType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ABTestService {

    private static final String AB_TEST_COUNT_KEY = "user_ab_test_count";

    private final StringRedisTemplate redisTemplate;

    public ABTestType assignABGroup() {
        Long count = redisTemplate.opsForValue().increment(AB_TEST_COUNT_KEY);
        return (count % 2 == 0) ? ABTestType.A : ABTestType.B;
    }
}
