package com.myZipPlan.server.logback;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@ActiveProfiles("prod") // 운영 환경 활성화
@TestPropertySource(properties = "logging.config=classpath:logback-spring.xml")
//@TestPropertySource(properties = "discord.webhook.url=https://discord.com/api/webhooks/1316396683260592178/zEYr2-WexcpVQ7U-BmQknm1LdeLvzSryM-t1dBI0PCh-eUcZCjqpSutqBBmnBKEpGXMF")
public class ProdProfileLoggingTest {

    private static final Logger logger = LoggerFactory.getLogger(ProdProfileLoggingTest.class);

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    private Environment environment;

    @Test
    void testActiveProfile() {
        // 현재 활성화된 프로파일 출력
        logger.info("Active profiles: {}", activeProfile);
        logger.info("Active profiles from Environment: {}", (Object) environment.getActiveProfiles());

        // Environment.getActiveProfiles() 배열을 문자열로 변환
        String activeProfilesString = String.join(",", environment.getActiveProfiles());
        logger.info("Converted active profiles: {}", activeProfilesString);
        // assertEquals로 확인
        assertEquals("Active profiles do not match", "prod", activeProfilesString);
    }

    @Test
    void testLoggingInProdProfile() {
        logger.error("This is an ERROR log for testing Discord webhook in prod.");
    }
}