package com.myZipPlan.server.common.aspects;

import com.myZipPlan.server.oauth.userInfo.SecurityUtils;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.userService.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ApiLoggingAspect {

    private final ApiLogRepository apiLogRepository;
    private final UserService userService;

    @AfterReturning("execution(* com.myZipPlan.server..*Controller.*(..))")
    public void logApiCall(JoinPoint jointPoint) {
        String methodName = jointPoint.getSignature().toShortString();
        String calledTime = LocalDateTime.now().toString();

        String username = "TEMPUSER";
        Long userId = 0L;

        try {
            String providerId = SecurityUtils.getProviderId();
            User user = userService.findUser(providerId);
            userId = user.getId();
            username = user.getName();
        } catch (IllegalStateException e) {
            log.warn("사용자 정보 없음! {} ", e.toString());
        }

        ApiLog apiLog = ApiLog.builder()
            .endpoint(methodName)
            .timestamp(calledTime)
            .userId(userId)
            .username(username)
            .build();

        apiLogRepository.save(apiLog);
    }

}
