package com.myZipPlan.server.logback;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("dev") // 개발 환경 활성화
@TestPropertySource(properties = "logging.config=classpath:logback-spring.xml")
public class DevProfileLoggingTest {

    private static final Logger logger = LoggerFactory.getLogger(DevProfileLoggingTest.class);

    @Test
    void testLoggingInDevProfile() {
        logger.error("This is an ERROR log for testing in dev (should not be sent to Discord).");
    }
}