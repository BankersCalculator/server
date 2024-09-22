package com.myZipPlan.server.common.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {

    // 유틸리티 메서드
    public static String calculateTimeAgo(LocalDateTime createdDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdDate, now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        long months = ChronoUnit.MONTHS.between(createdDate, now);
        long years = ChronoUnit.YEARS.between(createdDate, now);

        if (years > 0) {
            return years + "년 전"; // 1년 이상 차이
        } else if (months > 0) {
            return months + "개월 전"; // 1개월 이상 차이
        } else if (days > 0) {
            return days + "일 전"; // 1일 이상 차이
        } else if (hours > 0) {
            return hours + "시간 전"; // 1시간 이상 차이
        } else if (minutes > 0) {
            return minutes + "분 전"; // 1분 이상 차이
        } else {
            return "방금 전"; // 1분 미만
        }
    }
}
